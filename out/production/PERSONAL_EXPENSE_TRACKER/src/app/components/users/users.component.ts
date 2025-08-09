import {Component, inject, signal} from '@angular/core';
import {User, UserService} from '../../shared/services/user.service';
import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {FormsModule} from '@angular/forms';
import {MatButton} from '@angular/material/button';

@Component({
             selector: 'app-users',
             imports: [MatCard,
                       MatCardTitle,
                       MatCardContent,
                       MatFormField,
                       MatInput,
                       MatLabel,
                       FormsModule,
                       MatButton],
             templateUrl: './users.component.html',
             styleUrl: './users.component.scss'
           }) export class UsersComponent {
  private userService = inject(UserService);

  user = signal<User | null>(null);
  errorMessage = signal<string>('');

  constructor() {
    this.loadUser(1);
  }

  loadUser(userId: number) {
    this.userService.getUserById(userId).subscribe({
                                                     next: (user) => {
                                                       console.log("Fetched single user:", user);
                                                       this.user.set(user);
                                                     },
                                                     error: (err) => {
                                                       console.error("Failed to load user!", err);
                                                       this.errorMessage.set("User not found");
                                                     }
                                                   });
  }

  get userData(): User {
    return this.user() ??
      {
        user_id: 0,
        user_name: 'Guest User',
        email: ''
      };
  }

  updateUser() {
    if (this.user()) {
      this.userService.updateUser(this.user()!).
        subscribe({
                    next: (updatedUser) => console.log('User updated successfully!',
                                                       updatedUser),
                    error: (err) => console.error('Failed to update user!',
                                                  err)
                  });
    }
  }
}
