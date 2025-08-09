import {Component, inject, signal} from '@angular/core';
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

  handleLogout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
