import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

interface FAQ {
  id?: number;
  title: string;
  description: string;
  type?: string;
  active?: boolean;
}

@Component({
  selector: 'app-admin-faqs',
  templateUrl: './faq.html',
  styleUrls: ['./faq.scss'],
  imports: [FormsModule, CommonModule]
})
export class AdminFaqsComponent implements OnInit {
  faqs: FAQ[] = [];
  newFaq: FAQ = { title: '', description: '', type: 'FAQ', active: true };
  editFaq: FAQ | null = null;

  private baseUrl = 'http://localhost:8081/api/home-sections/faqs'; // correct base URL

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadFaqs();
  }

loadFaqs() {
  this.http.get<FAQ[]>('http://localhost:8081/api/home-sections/active/type/FAQ')
    .subscribe({
      next: data => this.faqs = data,
      error: err => console.warn('Failed to fetch FAQs', err)
    });
}


  startEdit(faq: FAQ) {
    this.editFaq = { ...faq };
  }

  cancelEdit() {
    this.editFaq = null;
  }

  updateFaq() {
    if (!this.editFaq || !this.editFaq.id) return;
    this.http.put(`${this.baseUrl}/${this.editFaq.id}`, this.editFaq)
      .subscribe({
        next: () => {
          this.loadFaqs();
          this.editFaq = null;
        },
        error: err => console.error('Update failed', err)
      });
  }

  addFaq() {
    this.http.post(this.baseUrl, this.newFaq)
      .subscribe({
        next: () => {
          this.loadFaqs();
          this.newFaq = { title: '', description: '', type: 'FAQ', active: true };
        },
        error: err => console.error('Add failed', err)
      });
  }

  deleteFaq(faq: FAQ) {
    if (!faq.id) return;
    this.http.delete(`${this.baseUrl}/${faq.id}`)
      .subscribe({
        next: () => this.loadFaqs(),
        error: err => console.error('Delete failed', err)
      });
  }
}
