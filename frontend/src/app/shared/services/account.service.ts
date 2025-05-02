import {inject, Injectable, signal, WritableSignal} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
              providedIn: 'root'
            })
export class AccountService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/users/balance';

  accounts: WritableSignal<{ accountType: string; balance: number }[]> = signal([]);

  loadAccounts(userId: number) {
    this.http.get<{ accounts: { accountType: string; balance: number }[] }>(`${this.apiUrl}/${userId}`)
      .subscribe({
                   next: response => {
                     console.log("Fetched accounts from API:", response.accounts); // ✅ Debugging API response
                     this.accounts.set(response.accounts); // ✅ Update signal
                     console.log("Accounts signal updated:", this.accounts()); // ✅ Confirm signal update
                   },
                   error: err => console.error("Failed to fetch account data!", err)
                 });
  }

}
