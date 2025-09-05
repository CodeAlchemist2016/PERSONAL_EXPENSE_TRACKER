import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {catchError, Observable, throwError} from 'rxjs';

@Injectable({
              providedIn: 'root'
            }) export class UserService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/users';

  getUser(userId: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${userId}`);
  }

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${user.user_id}`,
                               user);
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl)
      .pipe(
        catchError((error) => {
          console.error("API Error:", error);
          return throwError(() => new Error("Failed to load users"));
        })
    );
  }

  constructor() {
  }

  getUserById(userId: number) {
    return this.http.get<User>(`${this.apiUrl}/users/${userId}`);
  }
}

export interface User {
  user_id: number;
  user_name: string;
  email: string;
  password?: string;
}
