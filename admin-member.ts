import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminMemberService {
  private baseUrl = 'http://localhost:8081/api';

  constructor(private http: HttpClient) {}

  // ---- Matrimonial ----
  getPendingMatrimonial(): Observable<any> {
    return this.http.get(`${this.baseUrl}/matrimonial/pending`);
  }

  approveMatrimonial(id: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/matrimonial/approve/${id}`, {});
  }

  rejectMatrimonial(id: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/matrimonial/reject/${id}`, {});
  }

  // ---- Membership ----
  getPendingMembership(): Observable<any> {
    return this.http.get(`${this.baseUrl}/membership/pending`);
  }

  approveMembership(id: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/membership/approve/${id}`, {});
  }

  rejectMembership(id: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/membership/reject/${id}`, {});
  }
 getAllMembershipUsers(): Observable<any[]> {
  return this.http.get<any[]>(`${this.baseUrl}/membership/all`);
}

updateMembership(member: any): Observable<any> {
  return this.http.put(`${this.baseUrl}/membership/${member.id}`, member);
}

deleteMembership(id: number): Observable<any> {
  return this.http.delete(`${this.baseUrl}/membership/${id}`);
}

}
