SET GLOBAL log_bin_trust_function_creators = 1;


-- -------------------------------------------- INSERTS ---------------------------------------------------

INSERT INTO users (User_Id, name, email, Password, joinDate)
VALUES
    (1, 'Eugene Medvedev', 'eugene.medvedev@example.com', 'password123', '2025-04-22 10:00:00');


INSERT INTO accounts (Account_Id, User_Id, Account_Type, Balance)
VALUES
    (1, 1, 'SAVINGS', 1000.00),
    (2, 1, 'CHECKING', 500.00),
    (3, 1, 'SAVINGS', 7500.00),
    (4, 1, 'CHECKING', 2000.00),
    (5, 1, 'SAVINGS', 3000.50),
    (6, 1, 'CHECKING', 1500.00),
    (7, 1, 'SAVINGS', 2500.00),
    (8, 1, 'CHECKING', 1200.00),
    (9, 1, 'SAVINGS', 5000.00),
    (10, 1, 'CHECKING', 1800.00);


INSERT INTO categories (Category_Id, Category_Name, Category_Type, Description, Hierarchy_Level, Parent_Category_Id, Priority_Level, Hierarchy_Path)
VALUES
    (1, 'Food', 'EXPENSE', 'All food-related expenses, including groceries and dining.', 1, NULL, 3, '/Food'),
    (2, 'Entertainment', 'EXPENSE', 'Expenses for leisure activities like movies and concerts.', 1, NULL, 4, '/Entertainment'),
    (3, 'Income', 'INCOME', 'Sources of income including salaries and bonuses.', 1, NULL, 1, '/Income'),
    (4, 'Groceries', 'EXPENSE', 'Daily essentials and supermarket shopping.', 2, 1, 2, '/Food/Groceries'),
    (5, 'Dining', 'EXPENSE', 'Eating out, fast food, and fine dining.', 2, 1, 4, '/Food/Dining'),
    (6, 'Movies', 'EXPENSE', 'Tickets for cinema and streaming services.', 2, 2, 3, '/Entertainment/Movies'),
    (7, 'Concerts', 'EXPENSE', 'Live performances and music events.', 2, 2, 4, '/Entertainment/Concerts'),
    (8, 'Salaries', 'INCOME', 'Regular income from work or contracts.', 2, 3, 1, '/Income/Salaries'),
    (9, 'Bonuses', 'INCOME', 'Additional income, such as year-end bonuses.', 2, 3, 2, '/Income/Bonuses'),
    (10, 'Vegetables', 'EXPENSE', 'Fresh vegetables purchased for home cooking.', 3, 4, 2, '/Food/Groceries/Vegetables'),
    (11, 'Fruits', 'EXPENSE', 'Seasonal and fresh fruits.', 3, 4, 3, '/Food/Groceries/Fruits'),
    (12, 'Fast Food', 'EXPENSE', 'Quick meals from fast-food outlets.', 3, 5, 4, '/Food/Dining/FastFood'),
    (13, 'Fine Dining', 'EXPENSE', 'Elegant and upscale dining experiences.', 3, 5, 5, '/Food/Dining/FineDining');

INSERT INTO PaymentMethod (Method_Name) VALUES
                                            ('Cash'),
                                            ('Bank Card'),
                                            ('Credit Card'),
                                            ('Mobile Payment'),
                                            ('PayPal'),
                                            ('Wire Transfer'),
                                            ('Check'),
                                            ('Gift Card'),
                                            ('Cryptocurrency'),
                                            ('Bank Deposit');



-- 1 - TRIGGER


CREATE TRIGGER UpdateAccountBalance
    AFTER INSERT ON Transactions
    FOR EACH ROW
BEGIN
    DECLARE v_CategoryType VARCHAR(20);

    -- Pre-fetch category type
    SELECT Category_Type INTO v_CategoryType FROM Categories WHERE Category_ID = NEW.Category_ID;

    -- Update balance
    UPDATE Accounts
    SET Balance =
            CASE
                WHEN v_CategoryType = 'Expense' THEN Balance - NEW.Amount
                ELSE Balance + NEW.Amount
                END
    WHERE Account_ID = NEW.Account_ID;
END;

-- 2 - TRIGGER

CREATE TRIGGER Budget_Check
    BEFORE INSERT ON Transactions
    FOR EACH ROW
BEGIN
    -- Get max spending for the user/category
    SET @Max_Spending = (SELECT Max_Spending FROM BudgetLimits WHERE User_ID = NEW.User_ID AND Category_ID = NEW.Category_ID);

    -- Trigger error if spending exceeds the limit
    IF NEW.Amount > @Max_Spending THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Budget limit exceeded!';
    END IF;
END;

-- 3 - TRIGGER to populate transationhistory after transaction insert

CREATE TRIGGER AfterTransactionInsert
    AFTER INSERT ON Transactions
    FOR EACH ROW
BEGIN
    INSERT INTO TransactionsHistory (Transaction_ID, User_ID, Account_ID, Category_ID, PaymentMethod_ID, Quantity, Price, Amount, Transaction_Date, Description)
    VALUES (
               NEW.Transaction_ID,
               NEW.User_ID,
               NEW.Account_ID,
               NEW.Category_ID,
               NEW.PaymentMethod_ID,
               NEW.Quantity,
               NEW.Price,
               NEW.Amount,
               NEW.Transaction_Date,
               CONCAT('Trigger-logged: ', NEW.Description)
           );
END;



-- ------------------------------------------- PROCEDURES --------------------------------------------

CREATE PROCEDURE PopulateTransactions()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE v_User_ID INT;
    DECLARE v_Account_ID INT;

    WHILE i < 200 DO
            SET v_User_ID = (SELECT User_ID FROM users ORDER BY RAND() LIMIT 1);
            SET v_Account_ID = (SELECT Account_ID FROM accounts WHERE User_ID = v_User_ID ORDER BY RAND() LIMIT 1);

            IF v_Account_ID IS NOT NULL THEN
                INSERT INTO Transactions (User_ID, Account_ID, Category_ID, PaymentMethod_ID, Quantity, Price, Description)
                VALUES (
                           v_User_ID,
                           v_Account_ID,
                           FLOOR(1 + (RAND() * 10)),
                           FLOOR(1 + (RAND() * 10)),
                           FLOOR(1 + (RAND() * 5)),
                           ROUND(RAND() * 100 + 1, 2),
                           CONCAT('Auto-generated transaction #', i+1)
                       );
            END IF;

            SET i = i + 1;
        END WHILE;
END;

drop procedure PopulateTransactions;



CREATE PROCEDURE PopulateTransactions()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE v_User_ID INT DEFAULT NULL;
    DECLARE v_Account_ID INT DEFAULT NULL;
    DECLARE v_Category_ID INT DEFAULT NULL;
    DECLARE v_PaymentMethod_ID INT DEFAULT NULL;
    DECLARE v_Quantity INT DEFAULT NULL;
    DECLARE v_Price DECIMAL(10,2) DEFAULT NULL;

    -- Label the loop explicitly for proper iteration
    transaction_loop: WHILE i < 200 DO
            -- Step 1: Fetch a single random User_ID
            SELECT User_ID INTO v_User_ID
            FROM users
            WHERE User_ID IS NOT NULL
            ORDER BY RAND()
            LIMIT 1;

            -- Step 2: Fetch a single random Account_ID associated with the User_ID
            SELECT Account_ID INTO v_Account_ID
            FROM accounts
            WHERE User_ID = v_User_ID
            ORDER BY RAND()
            LIMIT 1;

            -- Ensure Account_ID is valid before proceeding
            IF v_Account_ID IS NULL THEN
                ITERATE transaction_loop; -- Skip to the next loop iteration
            END IF;

            -- Step 3: Fetch a single random Category_ID
            SELECT Category_ID INTO v_Category_ID
            FROM categories
            WHERE Category_ID IS NOT NULL
            ORDER BY RAND()
            LIMIT 1;

            -- Ensure Category_ID is valid before proceeding
            IF v_Category_ID IS NULL THEN
                ITERATE transaction_loop;
            END IF;

            -- Step 4: Generate random PaymentMethod_ID
            SET v_PaymentMethod_ID = FLOOR(1 + (RAND() * 10)); -- Random value between 1 and 10

            -- Step 5: Generate random Quantity and Price
            SET v_Quantity = FLOOR(1 + (RAND() * 5)); -- Quantity between 1 and 5
            SET v_Price = ROUND(RAND() * 100 + 1, 2); -- Price between 1.00 and 100.00

            -- Step 6: Insert into Transactions table
            INSERT INTO transactions (User_ID, Account_ID, Category_ID, PaymentMethod_ID, Quantity, Price, Transaction_Date, Description)
            VALUES (
                       v_User_ID,
                       v_Account_ID,
                       v_Category_ID,
                       v_PaymentMethod_ID,
                       v_Quantity,
                       v_Price,
                       NOW(),
                       CONCAT('Auto-generated transaction #', i+1)
                   );

            -- Step 7: Fetch the last inserted Transaction_ID
            SET @LastTransactionID = LAST_INSERT_ID();

            -- Step 8: Insert into TransactionHistory table
            INSERT INTO transactionhistory (Transaction_ID, User_ID, Account_ID, Category_ID, PaymentMethod_ID, Quantity, Price, Amount, Transaction_Date, Description)
            VALUES (
                       @LastTransactionID,
                       v_User_ID,
                       v_Account_ID,
                       v_Category_ID,
                       v_PaymentMethod_ID,
                       v_Quantity,
                       v_Price,
                       v_Quantity * v_Price,
                       NOW(),
                       CONCAT('Auto-logged history for transaction #', i+1)
                   );

            -- Step 9: Increment the counter
            SET i = i + 1;
        END WHILE;
END;

show tables;
select * from transactionhistory;
show grants for current_user;
flush tables;
repair table transactionhistory;
describe transactionhistory;
check table transactionhistory;


CREATE PROCEDURE GetAccountActivity(
    IN selected_user INT,
    IN selected_account INT,
    IN start_date DATE,
    IN end_date DATE
)
BEGIN
    -- Generate the transaction report for ALL users/accounts when NULL is passed
    SELECT T.User_ID,
           T.Account_ID,
           T.Transaction_Date,
           T.Description,
           C.Category_Name,
           P.Method_Name,
           T.Amount,
           A.Balance AS Updated_Balance
    FROM Transactions T
             JOIN Categories C ON T.Category_ID = C.Category_ID
             JOIN PaymentMethod P ON T.PaymentMethod_ID = P.PaymentMethod_ID
             JOIN Accounts A ON T.Account_ID = A.Account_ID
    WHERE T.Transaction_Date BETWEEN start_date AND end_date
      AND (selected_user IS NULL OR T.User_ID = selected_user)
      AND (selected_account IS NULL OR T.Account_ID = selected_account)
    ORDER BY T.Transaction_Date ASC;
END;

-