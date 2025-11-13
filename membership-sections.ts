import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

type SectionType =
  | 'MEMBERSHIP_BANNER'
  | 'MEMBERSHIP_ANNOUNCEMENT'
  | 'MEMBERSHIP_EVENT'
  | 'MEMBERSHIP_ACTIVITY'
  | 'MEMBERSHIP_CARD'
  | 'REFERRAL_CARD'
  | 'MEMBERSHIP_PHOTO';

@Component({
  selector: 'app-membership-sections',
  standalone: true,
  templateUrl: './membership-sections.html',
  styleUrls: ['./membership-sections.scss'],
  imports: [FormsModule, CommonModule],
})
export class MembershipSectionsComponent implements OnInit {
  type: SectionType = 'MEMBERSHIP_EVENT';
  sections: any[] = [];

  editingSection: any = null;
  title = '';
  description = '';
  route = '';
  selectedFile: File | null = null;        // for single file sections
selectedFiles: File[] = [];


  private apiUrl = 'http://localhost:8081/api/home-sections';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadSections();
  }

  loadSections(): void {
    this.http.get<any[]>(`${this.apiUrl}/type/${this.type}`).subscribe({
      next: (data) => {
        this.sections = data.map(s => ({
          ...s,
          imageUrls: s.imageUrl ? [s.imageUrl] : []
        }));
      },
      error: (err) => console.error('Failed to load sections', err),
    });
  }

onFilesSelected(event: Event) {
  const input = event.target as HTMLInputElement;
  if (!input.files) return;

  if (this.type === 'MEMBERSHIP_PHOTO') {
    this.selectedFiles = Array.from(input.files); // multiple files
    this.selectedFile = null;
  } else {
    this.selectedFile = input.files[0] || null;  // single file
    this.selectedFiles = [];
  }
}


  editSection(section: any) {
    this.editingSection = section;
    this.title = section.title;
    this.description = section.description;
    this.route = section.route || '';
    this.selectedFile = null;
  }

  saveSection() {
    if (!this.editingSection) return;

    const formData = new FormData();
    formData.append('title', this.title);
    formData.append('description', this.description || '');
    formData.append('route', this.route || '');
    formData.append('type', this.type);

    if (this.selectedFile) {
      formData.append('image', this.selectedFile);
    }

    this.http.put(`${this.apiUrl}/${this.editingSection.id}`, formData).subscribe({
      next: () => {
        this.resetForm();
        this.loadSections();
      },
      error: err => console.error('Update failed', err)
    });
  }

addSection() {
  const formData = new FormData();
  formData.append('title', this.title);
  formData.append('description', this.description || '');
  formData.append('route', this.route || '');
  formData.append('type', this.type);

  if (this.type === 'MEMBERSHIP_PHOTO') {
    this.selectedFiles.forEach(file => formData.append('files', file)); // multiple
    this.http.post(`${this.apiUrl}/photos`, formData).subscribe({
      next: () => { this.resetForm(); this.loadSections(); },
      error: err => console.error('Add photo failed', err)
    });
  } else {
    if (this.selectedFile) formData.append('image', this.selectedFile);
    this.http.post(`${this.apiUrl}/add`, formData).subscribe({
      next: () => { this.resetForm(); this.loadSections(); },
      error: err => console.error('Add section failed', err)
    });
  }
}
  deleteSection(id: number) {
    if (!confirm('Are you sure to delete this section?')) return;
    this.http.delete(`${this.apiUrl}/${id}`).subscribe({
      next: () => this.loadSections(),
      error: (err) => console.error('Delete failed', err),
    });
  }

  resetForm() {
    this.editingSection = null;
    this.title = '';
    this.description = '';
    this.route = '';
    this.selectedFile = null;
  }

  getImageUrl(section: any) {
    return section.imageUrls && section.imageUrls.length > 0
      ? `http://localhost:8081${section.imageUrls[0]}`
      : 'default-section.jpg';
  }
}
