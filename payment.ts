  import { Injectable } from '@angular/core';
  import { HttpClient } from '@angular/common/http';
  import { Observable } from 'rxjs';

  export interface PaymentDetails {
    userType: string;
    fullName: string;
    phone: string;
    membershipPlan: string;
    amount: number;
    paymentId?: string;
    status?: string;
    isYadav: boolean;
  }

  @Injectable({ providedIn: 'root' })
  export class PaymentService {
    private baseUrl = 'http://localhost:8081/api/payments';

    constructor(private http: HttpClient) {}

    getPaymentDetails(phone: string, userType: string): Observable<PaymentDetails> {
      return this.http.get<PaymentDetails>(`${this.baseUrl}/amount/${phone}?userType=${userType}`);
    }

    markPaymentSuccess(phone: string, paymentId: string, userType: string): Observable<any> {
      return this.http.post(`${this.baseUrl}/mark-done`, { phone, paymentId, userType });
    }
      // Admin: get all payments
    getAllPayments(): Observable<PaymentDetails[]> {
      return this.http.get<PaymentDetails[]>(`${this.baseUrl}`);
    }


  }
