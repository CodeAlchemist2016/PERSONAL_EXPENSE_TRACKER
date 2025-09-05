export interface TransactionResponse {
  id?: number;
  amount: number;
  price: number;
  quantity: number;
  description: string;
  transactionDate: string;
  transactionType: 'INCOME' | 'EXPENSE';
  categoryName: string;
  accountType: string;
  paymentMethodName: string;
  userName: string;
  oldBalance: number;
  newBalance: number;
}

export interface Category {
  id?: number;
  name: string;
  description: string;
  priorityLevel: number;
  hierarchyLevel: number;
  parentCategory: string;
  hierarchyPath: string;
  type: 'INCOME' | 'EXPENSE';
}

export interface Account {
  id?: number;
  accountType: 'CHECKING' | 'SAVINGS' | 'INVESTMENT';
  balance: number;
  maxSpending: number;
  totalTransactions: number;
  accountNumber: string;
  bankName: string;
}

export interface User {
  id?: number;
  name: string;
  email: string;
  joinDate: string;
  accounts: Account[];
}

export interface PaymentMethod {
  id?: number;
  methodName: string;
}

export interface RecurringExpense {
  id?: number;
  description: string;
  amount: number;
  frequency: 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'YEARLY';
  startDate: string;
  nextDueDate: string;
  categoryName: string;
}

export interface ReportDTO {
  reportType: 'TRANSACTION' | 'BUDGET' | 'AUDIT' | 'RECURRING_EXPENSE';
  userId: number;
  startDate: string;
  endDate: string;
  totalIncome: number;
  totalExpenses: number;
  budgetLimit: number;
  remainingBudget: number;
  additionalData: Record<string, any>;
}

export interface TransactionHistory {
  historyId?: number;
  userName: string;
  accountType: string;
  categoryName: string;
  paymentMethodName: string;
  quantity: number;
  price: number;
  amount: number;
  transactionDate: string;
  description: string;
}
