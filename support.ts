import { Component } from '@angular/core';
import { SupportService } from '../../../core/support';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-support',
  standalone: true,
  templateUrl: './support.html',
  styleUrls: ['./support.scss'],
  imports: [FormsModule, CommonModule]
})
export class userSupportComponent {
  formData = { name: '', email: '', subject: '', message: '' };
  successMsg = '';
  errorMsg = '';
  loading = false;

  constructor(private supportService: SupportService) {}

  sendMessage() {
    if (!this.formData.name || !this.formData.email || !this.formData.subject || !this.formData.message) {
      this.errorMsg = 'Please fill all fields!';
      return;
    }

    this.loading = true;
    this.supportService.sendMessage(this.formData).subscribe({
      next: () => {
        this.successMsg = 'Your message has been sent!';
        this.errorMsg = '';
        this.loading = false;
        this.formData = { name: '', email: '', subject: '', message: '' };
      },
      error: () => {
        this.errorMsg = 'Something went wrong. Please try again.';
        this.loading = false;
      }
    });
  }
}
