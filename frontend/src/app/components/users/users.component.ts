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
  users = signal<User[]>([]);
  errorMessage = signal<string>('');

  constructor() {
    this.loadUser(1);
  }

  loadUser(userId: number) {
    this.userService.getUser(userId).
      subscribe({
                  next: (userData) => this.user.set(userData),
                  error: (err) => console.error('Failed to load user!',
                                                err)
                });
  }

  loadUsers() {
    this.userService.getUsers().
      subscribe({
                  next: (userList) => {
                    console.log("Fetched users:",
                                userList); // ✅ Debugging log
                    this.users.set(userList);
                  },
                  error: (err) => {
                    console.error("Failed to load users!",
                                  err);
                    this.errorMessage.set("Failed to load users"); // ✅ Set error message
                  }
                });
  }


  get userData(): User {
    return this.user() ??
      {
        id: 0,
        username: '',
        email: ''
      };
  }

  updateUser() {
    if (this.user()) {
      this.userService.updateUser(this.user()!).
        subscribe({
                    next: (updateUser) => console.log('User updated successfully!',
                                                      updateUser),
                    error: (err) => console.error('Failed to update user!',
                                                  err)
                  });
    }
  }

}
