import {inject, Injectable, signal, WritableSignal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Account} from '../../models/account.model';

@Injectable({
              providedIn: 'root'
            }) export class AccountService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/accounts';

  accountTypes: WritableSignal<string[]> = signal([]);

  accounts: WritableSignal<Account[]> = signal([]);

  // Load account types dynamically from the backend
  loadAccountTypes() {
    if (this.accountTypes().length ===
      0) {
      this.http.get<string[]>(`${this.apiUrl}/types`).
        subscribe({
                    next: response => this.accountTypes.set(response),
                    error: err => console.error("Failed to fetch account types",
                                                err)
                  });
    }
  }

  // Load accounts dynamically from the backend
  loadAccounts(userId: number) {
    if (this.accounts().length ===
      0) {
      this.http.get<Account[]>(`${this.apiUrl}/${userId}`).
        subscribe({
                    next: response => this.accounts.set(response),
                    error: err => console.error("Failed to fetch accounts!",
                                                err)
                  });
    }
  }

  // Create a new account
  createAccount(account: Omit<Account, 'id'>) {
    console.log("Sending account data:", account);
    return this.http.post<Account>(this.apiUrl,
                          account);
  }

  // Update an existing account
  updateAccount(id: number,
                updatedAccount: Account) {
    return this.http.put(`${this.apiUrl}/${id}`,
                         updatedAccount);
  }

  // Delete an account
  deleteAccount(id: number) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  getAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(this.apiUrl);
  }
}
