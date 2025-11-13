import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin-happy-story',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-happy-stories.html',
  styleUrls: ['./admin-happy-stories.scss']
})
export class AdminHappyStoryComponent implements OnInit {
  stories: any[] = [];
  newStory = { coupleName: '', message: '', photo: null as File | null };
  apiUrl = 'http://localhost:8081/api/happy-stories';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadStories();
  }

  // ✅ Load all stories
  loadStories() {
    this.http.get<any[]>(this.apiUrl).subscribe({
      next: (res) => (this.stories = res),
      error: (err) => console.error('Error loading stories:', err)
    });
  }

  // ✅ Handle file selection
  onFileSelected(event: any) {
    this.newStory.photo = event.target.files[0];
  }

  // ✅ Add new story (with optional photo)
  addStory() {
    if (!this.newStory.coupleName || !this.newStory.message) {
      alert('Please fill all required fields.');
      return;
    }

    const formData = new FormData();
    formData.append('coupleName', this.newStory.coupleName);
    formData.append('message', this.newStory.message);
    if (this.newStory.photo) formData.append('photo', this.newStory.photo);

    this.http.post(`${this.apiUrl}/add`, formData).subscribe({
      next: () => {
        alert('Story added successfully!');
        this.newStory = { coupleName: '', message: '', photo: null };
        this.loadStories();
      },
      error: (err) => {
        console.error('Error adding story:', err);
        alert('Failed to add story.');
      }
    });
  }

  // ✅ Delete story
  deleteStory(id: number) {
    if (!confirm('Are you sure you want to delete this story?')) return;

    this.http.delete(`${this.apiUrl}/${id}`).subscribe({
      next: () => {
        alert('Story deleted successfully!');
        this.loadStories();
      },
      error: (err) => console.error('Error deleting story:', err)
    });
  }
}
