import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule, JsonPipe } from '@angular/common';

@Component({
  selector: 'app-admin-search',
  templateUrl: './admin-search.html',
  styleUrls: ['./admin-search.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, JsonPipe],
})
export class AdminSearchComponent {
  searchValue: string = '';
  searchType: string = 'membership';
  searchBy: string = 'id'; // 'id' | 'phone' | 'paymentId'
  result: any = null;
  errorMsg: string = '';

  backendUrl = 'http://localhost:8081/api/admin/search';

  constructor(private http: HttpClient) {}

  search() {
    if (!this.searchValue) {
      this.errorMsg = 'Please enter a value to search.';
      this.result = null;
      return;
    }

    this.errorMsg = '';
    this.result = null;

    let url = '';

    if (this.searchType === 'membership' || this.searchType === 'matrimonial') {
      url = this.searchBy === 'id'
        ? `${this.backendUrl}/${this.searchType}/${this.searchValue}`
        : `${this.backendUrl}/${this.searchType}/phone/${this.searchValue}`;
    } else if (this.searchType === 'donation') {
      url = this.searchBy === 'id'
        ? `${this.backendUrl}/donation/${this.searchValue}`
        : `${this.backendUrl}/donation/payment/${this.searchValue}`;
    }

    this.http.get(url).subscribe({
      next: (data) => {
        this.result = data;
      },
      error: () => {
        this.errorMsg = `No ${this.searchType} record found for the given ${this.searchBy}.`;
      },
    });
  }
}
