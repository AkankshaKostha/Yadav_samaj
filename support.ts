import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class SupportService {
  private baseUrl = 'http://localhost:8081/api/support';

  constructor(private http: HttpClient) {}

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }

  reply(id: number, reply: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}/reply`, { reply });
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

   // 🔹 User: send message
  sendMessage(data: any): Observable<any> {
    return this.http.post(this.baseUrl, data);
  }
}
