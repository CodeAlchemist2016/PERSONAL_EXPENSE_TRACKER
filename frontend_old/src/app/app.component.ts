import {Component, effect, inject, signal} from '@angular/core';
import {Router, RouterLink, RouterOutlet} from '@angular/router';
import {NgIf} from '@angular/common';
import {AuthService} from './shared/services/auth.service';

@Component({
  selector: 'app-root',
             imports: [RouterOutlet,
                       RouterLink,
                       NgIf],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'expense-tracker';
  private authService = inject(AuthService);
  private router = inject(Router);

  loggedInUser = signal(this.authService.getLoggedInUser());

  constructor() {
    effect(() => {
      this.loggedInUser.set(this.authService.getLoggedInUser());
    });
  }

  handleLogout() {
    this.authService.logout();
    this.loggedInUser.set(null); // Обновляем сигнал после логаута
    this.router.navigate(['/login']);
  }
}
