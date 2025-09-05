import {Component, inject, signal} from '@angular/core';
import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {AccountService} from '../../shared/services/account.service';
import {CurrencyPipe, NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {RouterLink} from '@angular/router';
import {Account} from '../../models/account.model';

@Component({
             selector: 'app-accounts',
             standalone: true,
             templateUrl: './accounts.component.html',
             imports: [MatCard,
                       MatCardTitle,
                       MatCardContent,
                       CurrencyPipe,
                       NgIf,
                       NgForOf,
                       FormsModule,
                       RouterLink],
             styleUrls: ['./accounts.component.scss']
           }) export class AccountsComponent {
  private accountService = inject(AccountService);
  accounts = this.accountService.accounts;
  selectedAccountType = signal<string>('');
  accountTypes = this.accountService.accountTypes;

  newAccount: Omit<Account, 'id'> = {
    accountType: '',
    balance: 0,
    bankName: '',
    investmentType: '',
    portfolioValue: 0,
    accountNumber: '',
    user: {
      user_id: 0,
      user_name: ''
    }
  };

  constructor() {
    this.accountService.loadAccounts(1);
    this.accountService.loadAccountTypes();
  }

  addAccount() {
    this.accountService.createAccount(this.newAccount).
      subscribe({
                  next: (account: Account) => {
                    this.accounts.set([...this.accounts(),
                                       account]);
                  },
                  error: err => console.error("Failed to create account!",
                                              err)
                });
  }


  deleteAccount(id: number) {
    this.accountService.deleteAccount(id).
      subscribe({
                  next: () => {
                    alert("Account deleted successfully!");
                    this.accountService.loadAccounts(1);
                  },
                  error: err => console.error("Failed to delete account!",
                                              err)
                });
  }
}
