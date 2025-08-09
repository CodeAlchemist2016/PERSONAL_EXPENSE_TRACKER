import {Component, inject, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {TransactionService} from '../../shared/services/transactions.service';
import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {CurrencyPipe, DatePipe, NgForOf} from '@angular/common';
import {AccountService} from '../../shared/services/account.service';
import {HttpClient} from '@angular/common/http';


@Component({
             selector: 'app-transactions',
             imports: [FormsModule,
                       MatCard,
                       MatCardTitle,
                       MatCardContent,
                       CurrencyPipe,
                       NgForOf,
                       DatePipe],
             templateUrl: './transactions.component.html',
             styleUrls: ['./transactions.component.scss']
           }) export class TransactionsComponent {


  private accountService = inject(AccountService);
  private transactionService = inject(TransactionService);
  private http = inject(HttpClient);


  transaction = {
    userId: null,
    accountId: null,
    categoryId: null,
    paymentMethodId: null,
    quantity: 0,
    price: 0,
    description: ''
  };

  transactions = this.transactionService.transactions;


  users = signal<{ id: number; name: string }[]>([]);
  accounts = this.accountService.accounts;
  categories = this.transactionService.categories;
  paymentMethods = this.transactionService.paymentMethods;

  constructor() {
    this.loadUsers();
  }


  loadUsers() {
    this.http.get<{ id: number; name: string }[]>('http://localhost:8080/api/users')
    .subscribe({
                 next: response => {
                   this.users.set(response);

                   if (response.length > 0) {
                     const selectedUserId = response[0].id; // ✅ Dynamic selection
                     this.accountService.loadAccounts(selectedUserId);
                     this.transactionService.loadCategories();
                     this.transactionService.loadPaymentMethods();
                   }
                 },
                 error: err => console.error("Failed to load users!", err)
               });
  }


  submitTransaction() {
    console.log("Submitting transaction:",
                this.transaction);

    this.transactionService.submitTransaction(this.transaction).
      subscribe({
                  next: response => {
                    console.log("Transaction submitted successfully!",
                                response);

                    this.transactionService.loadTransactions(); // ✅ Refresh list

                    // ✅ Reset form
                    this.transaction =
                      {
                        userId: null,
                        accountId: null,
                        categoryId: null,
                        paymentMethodId: null,
                        quantity: 0,
                        price: 0,
                        description: ''
                      };
                  },
                  error: err => console.error("Failed to submit transaction!",
                                              err)
                });
  }

  ngOnInit() {
    this.transactionService.loadTransactions();
    console.log("Users:",
                this.users());
    console.log("Accounts:",
                this.accounts());
    console.log("Categories:",
                this.categories());
    console.log("Payment Methods:",
                this.paymentMethods());
  }

}
