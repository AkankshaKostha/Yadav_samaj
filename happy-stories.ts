import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HappyStory } from './happy-story.model';


@Injectable({ providedIn: 'root' })
export class HappyStoryService {
  private apiUrl = 'http://localhost:8081/api/happy-stories';

  constructor(private http: HttpClient) {}

  /** ✅ Get all happy stories (returns Observable) */
  getStories(): Observable<HappyStory[]> {
    return this.http.get<HappyStory[]>(this.apiUrl);
  }

  /** ✅ Add new story */
  addStory(formData: FormData): Observable<HappyStory> {
    return this.http.post<HappyStory>(`${this.apiUrl}/add`, formData);
  }

  /** ✅ Delete story */
  deleteStory(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
