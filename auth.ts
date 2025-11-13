// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8081/api/users';
  private userKey = 'loggedUser'; // store all info (name, role, etc.)

  constructor(private http: HttpClient) {}

  // ------------------- OTP METHODS -------------------

  sendOtp(phone: string): Promise<string> {
    return lastValueFrom(
      this.http.post(`${this.apiUrl}/send-otp?phone=${phone}`, null, { responseType: 'text' })
    );
  }

  verifyOtp(phone: string, otp: string): Promise<any> {
    return lastValueFrom(
      this.http.post<any>('http://localhost:8081/api/matrimonial/verify-otp', { phone, otp })
    );
  }

  // ------------------- USER STORAGE -------------------

  saveUser(user: { name: string; role: string }): void {
    localStorage.setItem(this.userKey, JSON.stringify(user));
  }

  getUser(): { name: string; role: string } | null {
    const stored = localStorage.getItem(this.userKey);
    return stored ? JSON.parse(stored) : null;
  }

  getUserName(): string {
    const u = this.getUser();
    return u ? u.name : 'User';
  }

  getUserRole(): string {
    const u = this.getUser();
    return u ? u.role : '';
  }
   login(phone: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { phone, password });
  }

  // Check if payment is done
  checkPayment(phone: string): Observable<any> {
    return this.http.get(`http://localhost:8081/api/payments/amount/${phone}`);
  }

  clearUser(): void {
    localStorage.removeItem(this.userKey);
  }
}
