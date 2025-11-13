import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class FooterService {
  private apiUrl = 'http://localhost:8081/api/footer'; // adjust to your backend URL

  constructor(private http: HttpClient) {}

  getFooterInfo(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }
}
