import {Component, inject, signal} from '@angular/core';
import {AuthService} from '../../shared/services/auth.service';
import {Router} from '@angular/router';
import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {FormsModule} from '@angular/forms';
import {MatInput} from '@angular/material/input';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-login',
             imports: [MatCard,
                       MatCardTitle,
                       MatCardContent,
                       MatFormField,
                       FormsModule,
                       MatLabel,
                       MatInput,
                       MatButton],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router =  inject(Router);

  username = signal('');
  password = signal('');

  handleLogin() {
    const user = {id: 1, username: this.username() };
    this.authService.login(user);
    this.router.navigate(['/dashboard']);
  }

}
