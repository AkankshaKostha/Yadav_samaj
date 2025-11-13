import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AdminDonation {
  id?: number;   // ✅ optional
  name: string;
  amount: number;
  message?: string;
}

@Injectable({ providedIn: 'root' })
export class DonationService {
  private baseUrl = 'http://localhost:8081/api/donations/admin';

  constructor(private http: HttpClient) {}

  // ✅ Get all admin donations
  getAllAdminDonations(): Observable<AdminDonation[]> {
    return this.http.get<AdminDonation[]>(this.baseUrl);
  }

  // ✅ Add new donation
  addAdminDonation(donation: AdminDonation): Observable<AdminDonation> {
    return this.http.post<AdminDonation>(this.baseUrl, donation);
  }

  // ✅ Update donation
  updateAdminDonation(id: number, donation: AdminDonation): Observable<AdminDonation> {
    return this.http.put<AdminDonation>(`${this.baseUrl}/${id}`, donation);
  }

  // ✅ Delete donation
  deleteAdminDonation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
