import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HomeSection } from './models';

@Injectable({ providedIn: 'root' })
export class HomeSectionService {
  private baseUrl = 'http://localhost:8081/api/home-sections';

  constructor(private http: HttpClient) {}

  /**
   * ✅ Get ALL home sections (banners + cards)
   */
  getAll(): Observable<HomeSection[]> {
    return this.http.get<HomeSection[]>(this.baseUrl);
  }
getBanners(): Observable<HomeSection[]> {
    return this.http.get<HomeSection[]>(`${this.baseUrl}/type/BANNER`);
  }

  addBanner(formData: FormData): Observable<any> {
    return this.http.post(`${this.baseUrl}/banner`, formData);
  }


  /**
   * ✅ Get home sections filtered by type (e.g., 'BANNER' or 'CARD')
   */
  getByType(type: string): Observable<HomeSection[]> {
    return this.http.get<HomeSection[]>(`${this.baseUrl}/type/${type}`);
  }

  /**
   * ✅ Alias for getAll() — some components may use this
   */
  getSections(): Observable<HomeSection[]> {
    return this.http.get<HomeSection[]>(this.baseUrl);
  }

  /**
   * ✅ Add a new home section (with image upload)
   */
  addSection(formData: FormData): Observable<HomeSection> {
    return this.http.post<HomeSection>(`${this.baseUrl}/add`, formData);
  }

  /**
   * ✅ Update an existing section by ID
   */
  updateSection(id: number, formData: FormData): Observable<HomeSection> {
    return this.http.put<HomeSection>(`${this.baseUrl}/${id}`, formData);
  }

  /**
   * ✅ Delete a section by ID
   */
  deleteSection(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  getAds(): Observable<HomeSection[]> {
  return this.getByType('AD');
}


getLeadership() {
  return this.http.get<any[]>('http://localhost:8081/api/home-sections/leadership');
}


  addLeadership(formData: FormData): Observable<any> {
    return this.http.post(`${this.baseUrl}/leadership`, formData);
  }

  updateLeadership(id: number, formData: FormData): Observable<any> {
    return this.http.put(`${this.baseUrl}/leadership/${id}`, formData);
  }

  deleteLeadership(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/leadership/${id}`);
  }
}
