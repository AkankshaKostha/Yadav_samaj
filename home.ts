// src/app/core/home/home-section.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HomeSection } from './models';

@Injectable({ providedIn: 'root' })
export class HomeSectionService {
  private apiUrl = 'http://localhost:8081/api/home-sections';

  constructor(private http: HttpClient) {}

   // ✅ Get all home sections (both banners + cards)
  getSections(): Observable<HomeSection[]> {
    return this.http.get<HomeSection[]>(this.apiUrl);
  }

  // ✅ Add new section
  addSection(formData: FormData): Observable<HomeSection> {
    return this.http.post<HomeSection>(`${this.apiUrl}/add`, formData);
  }

  // ✅ Delete section
  deleteSection(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
