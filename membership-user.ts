import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom, Observable } from 'rxjs';

export type UserStatus = 'PENDING' | 'APPROVED' | 'REJECTED';

export interface MembershipUser {
  id?: number;
  fullName: string;
  phone: string;
  email: string;
  dob: string;
  gender?: string;
  membershipType?: string;

  address?: string;
  approved?: boolean;
  membershipPaid?: boolean;
  role?: string;
  photoFileName?: string;
  status?: UserStatus;
membershipPlan?: '1_YEAR' | 'LIFETIME';

  title?: string;
profilePhoto?: string;
  pincode?: string;
  district?: string;
  state?: string;
  assemblyConstituency?: string;
  referralPhone?: string;
  referredBy?: string;

}

// Response for OTP requests
export interface OtpResponse {
  otp?: string;
  message: string;
}
export interface HomeSection {
  id?: number;
  title: string;
  description?: string;
  route?: string;
  type: string;
  imageUrl?: string;
  imageUrls?: string[];
  active: boolean;
}

// Response for verify OTP
export interface VerifyOtpResponse {
  verified: boolean;
  approved?: boolean;
  paymentDone?: boolean;
  message: string;
  status?: UserStatus;
  role?: string;
  phone?: string;
  gender?: string;
  fullName?: string;
}

@Injectable({
  providedIn: 'root'
})
export class MembershipUserService {
  private baseUrl = 'http://localhost:8081/api/membership';

  constructor(private http: HttpClient) {}

  /** Send OTP to phone */
  sendOtp(phone: string): Promise<OtpResponse> {
    return lastValueFrom(
      this.http.get<OtpResponse>(`${this.baseUrl}/send-otp/${phone}`)
    );
  }

  /** Verify OTP */
  verifyOtp(phone: string, otp: string): Promise<VerifyOtpResponse> {
    return lastValueFrom(
      this.http.post<VerifyOtpResponse>(`${this.baseUrl}/verify-otp`, { phone, otp })
    );
  }

  /** Register new membership */
  register(formData: FormData): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, formData);
  }

  /** Get status by phone or id */
  getStatus(identifier: string | number) {
    const url = typeof identifier === 'number'
      ? `${this.baseUrl}/${identifier}/status`
      : `${this.baseUrl}/status/${identifier}`;

    return lastValueFrom(
      this.http.get<{ status: string; approved: boolean; paymentDone: boolean }>(url)
    );
  }

  /** Get all users */

 getAllMembershipUsers(): Observable<any[]> {
    return this.http.get<any[]>('/api/membership/all');
  }
  getAllUsers(): Promise<MembershipUser[]> {
  return lastValueFrom(this.http.get<MembershipUser[]>(`${this.baseUrl}/all`));
}


// membership-user.service.ts
// membership-user.service.ts
// updateMembership(id: number, user: any, photo?: File) {
//   const formData = new FormData();
//   formData.append('user', JSON.stringify(user));
//   if (photo) {
//     formData.append('photo', photo);
//   }

//   return this.http.put(`http://localhost:8081/api/membership/update/${id}`, formData);
// }


getMembershipDetails(phone: string) {
  return this.http.get(`${this.baseUrl}/details/${phone}`).toPromise();
}

  deleteMembership(id: number): Observable<any> {
    return this.http.delete(`/api/membership/${id}`);
  }

updateMembership(id: number, formData: FormData) {
  return this.http.put(`${this.baseUrl}/update/${id}`, formData);
}

}
