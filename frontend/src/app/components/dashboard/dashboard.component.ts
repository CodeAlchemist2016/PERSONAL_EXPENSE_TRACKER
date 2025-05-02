import {Component, computed, inject, signal} from '@angular/core';

import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {CurrencyPipe} from '@angular/common';
import {BarChartModule, Color, ScaleType} from '@swimlane/ngx-charts';
import {AccountService} from '../../shared/services/account.service';
import {TransactionService} from '../../shared/services/transactions.service';

@Component({
             selector: 'app-dashboard', imports: [MatCard, MatCardTitle, MatCardContent, CurrencyPipe, BarChartModule],
             templateUrl: './dashboard.component.html', styleUrls: ['./dashboard.component.scss']
           }) export class DashboardComponent {
  colorScheme: Color = {
    name: 'customScheme', selectable: true, group: ScaleType.Ordinal, domain: ['#3498db', '#e74c3c', '#f39c12']
  };
  accounts = signal<{ accountType: string; balance: number }[]>([]); // Proper type
  totalBalance = computed(() => this.accounts().reduce((sum, account) => sum + account.balance, 0));
  private accountService = inject(AccountService);
  private transactionService = inject(TransactionService);
  transactionChartData = this.transactionService.transactions;

  constructor() {
    this.accountService.loadAccounts(1);
    this.accounts = this.accountService.accounts;
    this.transactionService.loadTransactions();
  }

  refreshChart() {
    this.transactionChartData.set([...this.transactionChartData()]);
  }
}
