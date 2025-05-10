import {inject, Injectable, signal, WritableSignal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Transaction} from '../../models/transaction.model';

@Injectable({
              providedIn: 'root'
            }) export class TransactionService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/transactions';

  transactions: WritableSignal<{
    historyId: number;
    userName: string;
    accountType: string;
    categoryName: string;
    paymentMethodName: string;
    quantity: number;
    price: number;
    amount: number;
    transactionDate: string;
    description?: string;
  }[]> = signal([]);
  categories: WritableSignal<{ id: number; name: string }[]> = signal([]);
  paymentMethods: WritableSignal<{ id: number; methodName: string }[]> = signal([])

  loadTransactions() {
    this.http.get<{
      historyId: number;
      userName: string;
      accountType: string;
      categoryName: string;
      paymentMethodName: string;
      quantity: number;
      price: number;
      amount: number;
      transactionDate: string;
      description?: string;
    }[]>(this.apiUrl).
      subscribe({
                  next: response => {
                    const formattedResponse = response.map(transaction => ({
                      historyId: transaction.historyId,
                      userName: transaction.userName,
                      accountType: transaction.accountType,
                      categoryName: transaction.categoryName,
                      paymentMethodName: transaction.paymentMethodName,
                      quantity: transaction.quantity,
                      price: transaction.price,
                      amount: transaction.amount,
                      transactionDate: transaction.transactionDate,
                      description: transaction.description || 'No description'
                    }));

                    console.log("Formatted Transactions:", formattedResponse); // ✅ Debugging check
                    this.transactions.set(formattedResponse);
                  },
                  error: err => console.error("Failed to fetch transactions!", err)
                });
  }

  loadCategories() {
    this.http.get<{ id: number; name: string }[]>('http://localhost:8080/api/categories').
      subscribe({
                  next: response => this.categories.set(response),
                  error: err => console.error("Failed to load categories!",
                                              err)
                });
  }


  loadPaymentMethods() {
    this.http.get<{ content: {id: number; methodName: string }[]}>('http://localhost:8080/api/payment-methods').
      subscribe({
                  next: response => {
                    console.log("Payment methods fetched:",
                                response); // ✅ Debug data structure
                    this.paymentMethods.set(response.content);
                  },
                  error: err => console.error("Failed to load payment methods!",
                                              err)
                });
  }

  submitTransaction(transaction: any) {
    return this.http.post("http://localhost:8080/api/transactions/create",
                          transaction);
  }

  getRecentTransactions(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.apiUrl}/recent`)
  }

}
