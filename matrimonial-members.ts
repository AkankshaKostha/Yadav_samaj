import { Component, OnInit } from '@angular/core';
import { AdminMemberService } from '../../../../core/admin-member';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-matrimonial-members',
  templateUrl: './matrimonial-members.html',
  styleUrls: ['./matrimonial-members.scss'],
   imports:[CommonModule, FormsModule]
})
export class Matrimonial_MembersComponent implements OnInit {
  pendingUsers: any[] = [];

  constructor(private adminService: AdminMemberService) {}

  ngOnInit(): void {
    this.loadPending();
  }

  loadPending() {
    this.adminService.getPendingMatrimonial().subscribe({
      next: (data) => this.pendingUsers = data,
      error: (err) => console.error(err)
    });
  }

  approve(id: number) {
    this.adminService.approveMatrimonial(id).subscribe(() => this.loadPending());
  }

  reject(id: number) {
    this.adminService.rejectMatrimonial(id).subscribe(() => this.loadPending());
  }
}
