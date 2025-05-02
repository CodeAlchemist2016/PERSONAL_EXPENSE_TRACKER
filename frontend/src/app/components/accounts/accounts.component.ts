import {Component, inject} from '@angular/core';
import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {AccountService} from '../../shared/services/account.service';
import {CurrencyPipe, NgForOf, NgIf} from '@angular/common';

@Component({
             selector: 'app-accounts',
             standalone: true,
             templateUrl: './accounts.component.html',
             imports: [
               MatCard,
               MatCardTitle,
               MatCardContent,
               CurrencyPipe,
               NgIf,
               NgForOf
             ],
             styleUrls: ['./accounts.component.scss']
           })
export class AccountsComponent {
  private accountService = inject(AccountService);
  accounts = this.accountService.accounts; // ✅ Connects signal from service

  constructor() {
    this.accountService.loadAccounts(1); // ✅ Load data for a user (replace with dynamic user ID)
  }
}
