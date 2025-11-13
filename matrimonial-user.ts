  import { membershipPlans } from './premium-plans';
  import { Injectable } from '@angular/core';
  import { HttpClient, HttpErrorResponse } from '@angular/common/http';
  import { lastValueFrom } from 'rxjs';

  export type UserStatus = 'PENDING' | 'APPROVED' | 'REJECTED';

  export interface MatrimonialUser {
    id?: number;
    fullName: string;
    phone: string;
    email: string;
    dob: string;
    age?: number | null;
    gender?: string;
    education?: string;
    address?: string;

    role?: string;

    membershipPlan?:   "1_YEAR" | "LIFETIME";
    approved?: boolean;
    story?: string;
    occupation?: string;
    income?: number;
    maritalStatus?: string;
    motherTongue?: string;
    hobbies?: string;
    religion?: string;
    caste?: string;
    diet?: string;
    lifestyle?: string;
    height?: number;
    weight?: number;
    fatherName?: string;
    motherName?: string;
    brothers?: number;
    sisters?: number;
    city?: string;
    state?: string;
    country?: string;
    bloodGroup?: string;
    paymentDone?: boolean;
    currentOtp?: string;
    otpGeneratedAtMillis?: number;
    paymentAmount?: number;
    membershipType?: 'MATRIMONIAL' | 'GENERAL'; // Which type of membership
  photoFileName?: string;
    membershipAmount?: number;
    photoPath?: string; // store filename returned by backend
    status?: 'PENDING' | 'APPROVED' | 'REJECTED';
     isYadav?: boolean;
  }

  // OTP verification response
  export interface VerifyOtpResponse {
    verified: boolean;
    message: string;
    status?: UserStatus;
    role?: string;
    phone?: string;
    paymentDone?: boolean;
    membershipPlan?: "1_MONTH" | "1_YEAR" | "LIFETIME";

  }

  @Injectable({
    providedIn: 'root'
  })
  export class MatrimonialUserService {
    private baseUrl = 'http://localhost:8081/api/matrimonial';

    constructor(private http: HttpClient) {}

    // ---------------- Register ----------------
    async register(user: MatrimonialUser, photoFile?: File): Promise<MatrimonialUser> {
      const formData = new FormData();
      formData.append('user', new Blob([JSON.stringify(user)], { type: 'application/json' }));
      if (photoFile) {
        formData.append('photo', photoFile);
      }

      const result = await lastValueFrom(
        this.http.post<MatrimonialUser>(`${this.baseUrl}/register`, formData)
      );

      return result;
    }

    // ---------------- Send OTP ----------------
    async sendOtp(phone: string): Promise<{ otp?: string; message: string; success: boolean }> {
      try {
        const response = await lastValueFrom(
          this.http.get<{ otp?: string; message: string; success: boolean }>(`${this.baseUrl}/send-otp/${phone}`)
        );
        return response;
      } catch (error) {
        const err = error as HttpErrorResponse;
        if (err.status === 404) return { success: false, message: 'User not found. Please register first.' };
        if (err.status === 400 && err.error?.message?.includes('pending')) {
          return { success: false, message: 'Your account is pending admin approval.' };
        }
        return { success: false, message: 'Failed to send OTP. Please try again.' };
      }
    }

    // ---------------- Verify OTP ----------------
    async verifyOtp(phone: string, otp: string): Promise<VerifyOtpResponse> {
      const response = await lastValueFrom(
        this.http.post<VerifyOtpResponse>(`${this.baseUrl}/verify-otp`, { phone, otp })
      );
      return response;
    }

    // ---------------- Get approved but unpaid users ----------------
    async getApprovedButNotPaidMatrimonialUsers(): Promise<MatrimonialUser[]> {
      return await lastValueFrom(this.http.get<MatrimonialUser[]>(`${this.baseUrl}/approved-but-not-paid`));
    }

    // ---------------- User status ----------------
    async getStatus(identifier: string | number): Promise<{ status: string; approved: boolean; paymentDone: boolean }> {
      const url = typeof identifier === 'number'
        ? `${this.baseUrl}/${identifier}/status`
        : `${this.baseUrl}/status/${identifier}`;
      return await lastValueFrom(this.http.get<{ status: string; approved: boolean; paymentDone: boolean }>(url));
    }

    async getPendingUsers(): Promise<MatrimonialUser[]> {
      return await lastValueFrom(this.http.get<MatrimonialUser[]>(`${this.baseUrl}/pending`));
    }

    async approveUser(id: number): Promise<void> {
      await lastValueFrom(this.http.post<void>(`${this.baseUrl}/approve/${id}`, {}));
    }

    async rejectUser(id: number): Promise<void> {
      await lastValueFrom(this.http.post<void>(`${this.baseUrl}/reject/${id}`, {}));
    }

    async getAllUsers(): Promise<MatrimonialUser[]> {
      return await lastValueFrom(this.http.get<MatrimonialUser[]>(`${this.baseUrl}/all`));
    }

    async markPaymentDoneByPhone(phone: string): Promise<any> {
      return await lastValueFrom(this.http.post<any>(`${this.baseUrl}/payment-by-phone/${phone}`, {}));
    }

    selectPlan(userId: number, plan: string) {
    return this.http.post(
      `http://localhost:8081/api/matrimonial/select-plan/${userId}`,
      { plan }
    );
  }
  // MatrimonialUserService (add these at the end)
  async getUserByPhone(phone: string): Promise<MatrimonialUser> {
    return await lastValueFrom(
      this.http.get<MatrimonialUser>(`${this.baseUrl}/details/${phone}`)
    );
  }

  async updateUser(id: number, user: MatrimonialUser, photoFile?: File): Promise<MatrimonialUser> {
    const formData = new FormData();
    formData.append('user', new Blob([JSON.stringify(user)], { type: 'application/json' }));
    if (photoFile) {
      formData.append('photo', photoFile);
    }

    return await lastValueFrom(
      this.http.put<MatrimonialUser>(`${this.baseUrl}/${id}`, formData)
    );
  }

// Returns true if phone is already registered
async checkPhoneExists(phone: string): Promise<boolean> {
  try {
    const user = await this.http.get<MatrimonialUser>(`/api/matrimonial/details/${phone}`).toPromise();
    return !!user?.id;
  } catch (err) {
    return false; // Not found → phone is unique
  }
}


  }
