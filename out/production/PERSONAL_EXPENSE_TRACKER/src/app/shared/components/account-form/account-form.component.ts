import {ChangeDetectorRef, Component, effect, inject, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatOption, MatSelect} from '@angular/material/select';
import {AccountService} from '../../services/account.service';
import {MatInputModule} from '@angular/material/input';
import {AuthService} from '../../services/auth.service';
import {User, UserService} from '../../services/user.service';
import {MatButtonModule} from '@angular/material/button';

@Component({
             selector: 'app-account-form',
             imports: [FormsModule,
                       NgForOf,
                       MatCard,
                       MatCardTitle,
                       MatCardContent,
                       NgIf,
                       MatLabel,
                       MatInputModule,
                       MatButtonModule,
                       MatSelect,
                       MatOption,
                       MatFormField],
             templateUrl: './account-form.component.html',
             styleUrls: ['./account-form.component.scss']
           })

export class AccountFormComponent {
  private accountService = inject(AccountService);
  private authService = inject(AuthService);
  private userService = inject(UserService);
  private cdr = inject(ChangeDetectorRef);

  users = signal<User[]>([]);

  accountType = signal('');
  accountTypes = ['CHECKING',
                  'SAVINGS',
                  'CREDIT',
                  'INVESTMENT'];
  loggedInUser = signal(this.authService.getLoggedInUser());

  newAccount = {
    accountType: '',
    balance: 0,
    bankName: '',
    investmentType: '',
    portfolioValue: 0,
    accountNumber: '',
    maxSpending: 0,
    user: {
      user_id: this.loggedInUser()?.id ??
        0,
      user_name: ''
    }
  };

  constructor() {
    effect(() => {
      console.log("Selected Account Type:",
                  this.accountType());
      this.newAccount.accountType =
        this.accountType();
    });
    this.accountService.loadAccountTypes();
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getUsers().
      subscribe({
                  next: (userList) => {
                    console.log("Fetched users:",
                                userList);
                    this.users.set(userList);

                    // Ensure newAccount.user.id is set to a valid user ID
                    if (!userList.some(user => user.user_id ===
                      this.newAccount.user.user_id)) {
                      if (userList.length >
                        0) {
                        this.newAccount.user.user_id =
                          userList[0].user_id;
                        this.newAccount.user.user_name =
                          userList[0].user_name;
                      } else {
                        this.newAccount.user.user_id =
                          0;
                        this.newAccount.user.user_name =
                          "";
                      }
                    }

                    this.cdr.detectChanges();
                  },
                  error: (err) => console.error("Failed to load users!",
                                                err)
                });
  }


  handleSubmit() {
    if (!this.newAccount.user.user_id ||
      this.newAccount.user.user_id ===
      0) {
      console.error("User ID is missing or invalid!");
      return;
    }

    console.log("Submitting account:",
                this.newAccount);

    this.accountService.createAccount(this.newAccount).
      subscribe({
                  next: (account) => {
                    console.log("Account saved in DB:",
                                account);
                  },
                  error: (err) => console.error("Failed to create account!",
                                                err)
                });
  }

}
