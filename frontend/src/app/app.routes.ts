import {Routes} from '@angular/router';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {TransactionsComponent} from './components/transactions/transactions.component';
import {AccountsComponent} from './components/accounts/accounts.component';
import {AccountFormComponent} from './shared/components/account-form/account-form.component';
import {UsersComponent} from './components/users/users.component';
import {LoginComponent} from './components/login/login.component';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'transactions', component: TransactionsComponent },
  { path: 'accounts', component: AccountsComponent },
  { path: 'add-account', component: AccountFormComponent },
  { path: 'app-users', component: UsersComponent },
  { path: 'login', component: LoginComponent },
  { path: '**', redirectTo: '/dashboard' },
];
