import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PaymentDetails, PaymentService } from '../../../core/payment';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-admin-payments',
  standalone: true,
  imports: [CommonModule , FormsModule],
  templateUrl: './payments.html',
  styleUrls: ['./payments.scss']
})
export class PaymentsComponent implements OnInit {
  payments: PaymentDetails[] = [];
  loading = false;

  constructor(private paymentService: PaymentService) {}

  ngOnInit(): void {
    this.loading = true;
    this.paymentService.getAllPayments().subscribe({
      next: (res) => {
        this.payments = res;
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to fetch payments', err);
        this.loading = false;
      },
    });
  }
}
