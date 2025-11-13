import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TeamMember } from '../models/TeamMember';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TeamService {
  private baseUrl = 'http://localhost:8081/api/team';

  constructor(private http: HttpClient) {}

  // Get all members
  getAll(): Observable<TeamMember[]> {
    return this.http.get<TeamMember[]>(`${this.baseUrl}/all`);
  }

  // Add member
  add(data: FormData): Observable<TeamMember> {
    return this.http.post<TeamMember>(`${this.baseUrl}/add`, data);
  }

  // Update member
  update(id: number, data: FormData): Observable<TeamMember> {
    return this.http.put<TeamMember>(`${this.baseUrl}/${id}`, data);
  }

  // Delete member
  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/delete/${id}`);
  }

  // Get member by id
  getById(id: number): Observable<TeamMember> {
    return this.http.get<TeamMember>(`${this.baseUrl}/${id}`);
  }
}
