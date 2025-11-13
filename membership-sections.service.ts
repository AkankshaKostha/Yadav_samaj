import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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

@Injectable({ providedIn: 'root' })
export class MembershipSectionsService {
  private baseUrl = 'http://localhost:8081/api/home-sections';

  constructor(private http: HttpClient) {}

  getSectionsByType(type: string): Observable<HomeSection[]> {
    return this.http.get<HomeSection[]>(`${this.baseUrl}/type/${type}`);
  }

  getActiveSectionsByType(type: string): Observable<HomeSection[]> {
    return this.http.get<HomeSection[]>(`${this.baseUrl}/active/type/${type}`);
  }

  addSection(formData: FormData): Observable<HomeSection> {
    return this.http.post<HomeSection>(`${this.baseUrl}`, formData);
  }

  updateSection(id: number, formData: FormData): Observable<HomeSection> {
    return this.http.put<HomeSection>(`${this.baseUrl}/${id}`, formData);
  }

  deleteSection(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
  updateMembership(id: number, formData: FormData) {
  return this.http.put(`${this.baseUrl}/update/${id}`, formData);
}

}
