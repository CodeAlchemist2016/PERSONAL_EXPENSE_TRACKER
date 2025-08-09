import {Component, inject, signal} from '@angular/core';

import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {CurrencyPipe, DatePipe, NgForOf} from '@angular/common';
import {BarChartModule, Color, ScaleType} from '@swimlane/ngx-charts';
import {AccountService} from '../../shared/services/account.service';
import {TransactionService} from '../../shared/services/transactions.service';
import {Transaction} from '../../models/transaction.model';
import {Account} from '../../models/account.model';

@Component({
             selector: 'app-dashboard',
             imports: [MatCard,
                       MatCardTitle,
                       MatCardContent,
                       CurrencyPipe,
                       BarChartModule,
                       NgForOf,
                       DatePipe],
             templateUrl: './dashboard.component.html',
             styleUrls: ['./dashboard.component.scss']
           })

export class DashboardComponent {
  private accountService = inject(AccountService);
  private transactionService = inject(TransactionService);

  totalBalance = signal(0);
  accounts = signal<Account[]>([]);
  recentTransactions = signal<Transaction[]>([]);
  transactionChartData = signal<{name: string; value: number}[]>([]);
  colorScheme: Color = {
    domain: ['#007bff',
             '#ff4081',
             '#ffc107'],
    name: 'customScheme',
    selectable: true,
    group: ScaleType.Ordinal
  };


  constructor() {
    this.loadDashboardData();
  }


  private loadDashboardData() {
    this.accountService.getAccounts().
      subscribe({
                  next: (accounts: Account[]) => {
                    this.accounts.set(accounts);
                    this.totalBalance.set(accounts.reduce((sum: number,
                                                           acc: Account) => sum +
                                                            acc.balance,
                                                          0));
                  },
                  error: (err) => console.error('Failed to load accounts!',
                                                err)
                });

    this.transactionService.getRecentTransactions().
      subscribe({
                  next: (transactions) => {
                    this.recentTransactions.set(transactions);
                    this.transactionChartData.set(this.formatChartData(transactions));
                  },
                  error: (err) => console.error('Failed to load transactions!',
                                                err)
                });
  }

  formatChartData(transactions: Transaction[]): { name: string; value: number }[] {
    return transactions.map((tx: Transaction) => ({
      name: tx.date,
      value: tx.amount,
    }));
  }
}
