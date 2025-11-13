import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-matrimonial-sections',
  standalone: true,
  templateUrl: './matrimonial-sections.html',
  styleUrls: ['./matrimonial-sections.scss'],
  imports: [FormsModule, CommonModule],
})
export class MatrimonialSectionsComponent implements OnInit {
  // Default form values
  type: string = 'MATRIMONIAL_BANNER';
  title: string = '';
  description: string = '';
  route: string = '';
  selectedFile: File | null = null;

  sections: any[] = [];
  private apiUrl = 'http://localhost:8081/api/home-sections';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadSections();
  }

  /** ✅ Handle file input */
  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      console.log('📁 Selected file:', file.name);
    }
  }

  /** ✅ Fetch sections by type */
  loadSections(): void {
    if (!this.type) return;
    this.http.get<any[]>(`${this.apiUrl}/type/${this.type}`).subscribe({
      next: (data) => {
        this.sections = data;
        console.log('📦 Loaded sections:', data);
      },
      error: (err) => console.error('❌ Load failed:', err),
    });
  }

  /** ✅ Add new matrimonial banner/ad section */
  addSection(): void {
    // Validate required fields
    if (!this.title || !this.type) {
      alert('Please enter a title and select a type.');
      return;
    }

    const formData = new FormData();
    formData.append('title', this.title);
    formData.append('description', this.description || '');
    formData.append('route', this.route || '');
    formData.append('type', this.type);
    if (this.selectedFile) formData.append('image', this.selectedFile);

    console.log('📤 Sending data:', {
      title: this.title,
      description: this.description,
      route: this.route,
      type: this.type,
      image: this.selectedFile?.name,
    });

    // Don’t set headers manually — Angular auto-sets multipart
    this.http.post(`${this.apiUrl}/add`, formData).subscribe({
      next: (res) => {
        console.log('✅ Added successfully:', res);
        alert('Section added successfully!');
        this.resetForm();
        this.loadSections();
      },
      error: (err) => {
        console.error('❌ Add failed:', err);
        if (err.error?.message) alert(err.error.message);
        else alert('Upload failed. Please check backend logs.');
      },
    });
  }

  /** ✅ Delete a section */
  deleteSection(id: number): void {
    if (!confirm('Are you sure you want to delete this section?')) return;
    this.http.delete(`${this.apiUrl}/${id}`).subscribe({
      next: () => {
        console.log('✅ Deleted section:', id);
        this.loadSections();
      },
      error: (err) => console.error('❌ Delete failed:', err),
    });
  }

  /** ✅ Reset form after submission */
  private resetForm(): void {
    this.title = '';
    this.description = '';
    this.route = '';
    this.selectedFile = null;
  }
}
