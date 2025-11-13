import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { MatrimonialUser } from './matrimonial-user';
import { MembershipUser } from './membership-user';
import { HomeSection } from './models';


@Injectable({ providedIn: 'root' })
export class AdminService {
  private baseUrl = 'http://localhost:8081/api';

  constructor(private http: HttpClient) {}

  // ----------------- Dashboard & Users -----------------
  getDashboardStats() {
    return lastValueFrom(this.http.get<any>(`${this.baseUrl}/dashboard`));
  }

  getPendingMatrimonialUsers() {
    return lastValueFrom(this.http.get<MatrimonialUser[]>(`${this.baseUrl}/matrimonial/pending`));
  }

  getApprovedButNotPaidMatrimonialUsers() {
    return lastValueFrom(this.http.get<MatrimonialUser[]>(`${this.baseUrl}/matrimonial/approved-but-not-paid`));
  }

  getPendingMembershipUsers() {
    return lastValueFrom(this.http.get<MembershipUser[]>(`${this.baseUrl}/membership/pending`));
  }

  approveUser(id: number, type: 'membership' | 'matrimonial') {
    const url = type === 'membership'
      ? `${this.baseUrl}/membership/approve/${id}`
      : `${this.baseUrl}/matrimonial/approve/${id}`;
    return lastValueFrom(this.http.post(url, {}));
  }

  rejectUser(id: number, type: 'membership' | 'matrimonial') {
    const url = type === 'membership'
      ? `${this.baseUrl}/membership/reject/${id}`
      : `${this.baseUrl}/matrimonial/reject/${id}`;
    return lastValueFrom(this.http.post(url, {}));
  }

  markPaymentDone(phone: string, type: 'membership' | 'matrimonial', paymentId: string) {
    return lastValueFrom(this.http.post(`${this.baseUrl}/payments/mark-done`, { phone, paymentId }));
  }

  getPaymentAmount(phone: string, type: 'matrimonial' | 'membership') {
    return lastValueFrom(this.http.get<{ amount: number }>(`${this.baseUrl}/payments/amount/${phone}`));
  }

  // ----------------- Community Section -----------------

  // Get all community sections
getCommunitySections() {
    return lastValueFrom(this.http.get<any[]>(`${this.baseUrl}/home-sections/active/type/COMMUNITY`));
}

 addCommunity(formData: FormData) {
    return lastValueFrom(this.http.post(`${this.baseUrl}/home-sections/add`, formData));
}

updateCommunity(id: number, formData: FormData) {
    return lastValueFrom(this.http.put(`${this.baseUrl}/home-sections/${id}`, formData));
}

deleteCommunity(id: number) {
    return lastValueFrom(this.http.delete(`${this.baseUrl}/home-sections/${id}`));
}
  getActiveSectionsByType(type: string) {
    return lastValueFrom(this.http.get<HomeSection[]>(`${this.baseUrl}/active/type/${type.toUpperCase()}`));
  }
}
