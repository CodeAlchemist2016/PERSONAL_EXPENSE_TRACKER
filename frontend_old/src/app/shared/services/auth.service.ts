import {Injectable, signal} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private user = signal<{id: number; username: string } | null>(null);

  login(user: {id: number; username: string}) {
    this.user.set(user);
  }

  getLoggedInUser() {
    return this.user();
  }

  logout() {
    this.user.set(null);
  }

  constructor() { }
}
