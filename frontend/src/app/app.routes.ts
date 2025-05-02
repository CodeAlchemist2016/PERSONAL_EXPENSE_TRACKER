import {Routes} from '@angular/router';
import {AppComponent} from './app.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {TransactionsComponent} from './components/transactions/transactions.component';
import {AccountsComponent} from './components/accounts/accounts.component';

export const routes: Routes = [
  { path: '', component: AppComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'transactions', component: TransactionsComponent },
  { path: 'accounts', component: AccountsComponent },
  { path: '**', redirectTo: '/'}
];
