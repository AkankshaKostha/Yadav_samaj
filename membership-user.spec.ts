import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';

export type UserStatus = 'PENDING' | 'APPROVED' | 'REJECTED';

export interface MembershipUser {
  id?: number;
  fullName: string;
  phone: string;
  email: string;
  dob: string;
  membershipType?: string;
  membershipPlan?: string;
  address?: string;
  approved?: boolean;
  membershipPaid?: boolean;
  role?: string;
  photoFileName?: string;
  status?: UserStatus;
}

@Injectable({
  providedIn: 'root'
})
export class MembershipUserService {
  private baseUrl = 'http://localhost:8081/api/membership';

  constructor(private http: HttpClient) {}

  // Register new membership user
  register(user: MembershipUser) {
    return lastValueFrom(this.http.post(`${this.baseUrl}/register`, user));
  }

  // Send OTP
// membership-user.service.ts
sendOtp(phone: string): Promise<{ otp?: string; message: string }> {
  return lastValueFrom(
    this.http.post<{ otp?: string; message: string }>(
      `${this.baseUrl}/send-otp`,
      { phone }
    )
  );
}

  // Verify OTP
  verifyOtp(phone: string, otp: string) {
    return lastValueFrom(this.http.post(`${this.baseUrl}/verify-otp`, { phone, otp }));
  }

  // Get pending users
  getPendingUsers() {
    return lastValueFrom(this.http.get<MembershipUser[]>(`${this.baseUrl}/pending`));
  }

  // Approve user
  approveUser(id: number) {
    return lastValueFrom(this.http.post(`${this.baseUrl}/approve/${id}`, {}));
  }

  // Reject user
  rejectUser(id: number) {
    return lastValueFrom(this.http.post(`${this.baseUrl}/reject/${id}`, {}));
  }

  // Mark payment as done
  markPaymentDone(id: number) {
    return lastValueFrom(this.http.post(`${this.baseUrl}/payment-complete/${id}`, {}));
  }

  // Get user status by phone
  getStatus(phone: string) {
    return lastValueFrom(
      this.http.get<{ status: string; approved: boolean; paymentDone: boolean }>(
        `${this.baseUrl}/status/${phone}`
      )
    );
  }

  // Get all users
  getAllUsers() {
    return lastValueFrom(this.http.get<MembershipUser[]>(`${this.baseUrl}/all`));
  }
}
