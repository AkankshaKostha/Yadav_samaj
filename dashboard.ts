import { Component, OnInit, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.scss']
})
export class DashboardComponent implements OnInit, AfterViewInit {
  dashboard: any = {};
  earningsType: 'week' | 'month' | 'year' = 'year';
  chart: any;
  loading = false;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadDashboardStats();
  }

  ngAfterViewInit(): void {
    // Load chart after view initializes
    setTimeout(() => this.loadEarningsChart(), 300);
  }

  loadDashboardStats(): void {
    this.loading = true;
    this.http.get('http://localhost:8081/api/dashboard').subscribe({
      next: (data: any) => {
        this.dashboard = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Dashboard load failed', err);
        this.loading = false;
      },
    });
  }

  loadEarningsChart(): void {
    this.http.get<any>(`http://localhost:8081/api/dashboard/earnings?type=${this.earningsType}`).subscribe({
      next: (res) => this.renderChart(res),
      error: (err) => console.error('Earnings chart load failed', err)
    });
  }

  onTypeChange(): void {
    this.loadEarningsChart();
  }

  renderChart(res: any): void {
    const ctx = document.getElementById('earningsChart') as HTMLCanvasElement;
    if (!ctx) return;

    if (this.chart) this.chart.destroy();

    this.chart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: res.labels,
        datasets: [
          {
            label: 'Earnings',
            data: res.earnings,
            borderColor: '#3b82f6',
            backgroundColor: 'rgba(59, 130, 246, 0.2)',
            fill: true,
            tension: 0.4,
            pointRadius: 4,
            pointBackgroundColor: '#3b82f6',
          },
        ],
      },
      options: {
        responsive: true,
        plugins: {
          legend: { display: false },
          tooltip: {
            mode: 'index',
            intersect: false,
          },
        },
        scales: {
          x: { grid: { display: false } },
          y: { beginAtZero: true },
        },
      },
    });
  }
}
