import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminDonation, DonationService } from '../../../core/donation.service';

@Component({
  selector: 'app-admin-donation',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-donation.html',
  styleUrls: ['./admin-donation.scss']
})
export class AdminDonationComponent implements OnInit {
  donations: AdminDonation[] = [];
  donationForm: AdminDonation = { name: '', amount: 0, message: '' };
  editingDonation: AdminDonation | null = null;

  constructor(private donationService: DonationService) {}

  ngOnInit() {
    this.loadDonations();
  }

  loadDonations() {
    this.donationService.getAllAdminDonations().subscribe({
      next: (data: AdminDonation[]) => (this.donations = data),
      error: () => alert('Failed to load donations')
    });
  }

  saveDonation() {
    if (!this.donationForm.name || !this.donationForm.amount) {
      return alert('Please fill name and amount');
    }

    if (this.editingDonation) {
      // ✅ Update
      this.donationService.updateAdminDonation(this.editingDonation.id!, this.donationForm)
        .subscribe(() => {
          this.loadDonations();
          this.cancelEdit();
        });
    } else {
      // ✅ Add new
      this.donationService.addAdminDonation(this.donationForm)
        .subscribe(() => {
          this.loadDonations();
          this.donationForm = { name: '', amount: 0, message: '' };
        });
    }
  }

  editDonation(donation: AdminDonation) {
    this.editingDonation = donation;
    this.donationForm = { ...donation };
  }

  cancelEdit() {
    this.editingDonation = null;
    this.donationForm = { name: '', amount: 0, message: '' };
  }

  deleteDonation(id: number) {
    if (confirm('Are you sure you want to delete this donation?')) {
      this.donationService.deleteAdminDonation(id).subscribe(() => this.loadDonations());
    }
  }
}
