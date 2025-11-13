import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatrimonialUser } from '../../../core/matrimonial-user';
import { MembershipUser } from '../../../core/membership-user';
import { AdminService } from '../../../core/admin';

type CombinedUser =
  | (MatrimonialUser & { type: 'matrimonial' })
  | (MembershipUser & { type: 'membership' });

@Component({
  selector: 'app-members',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './members.html',
  styleUrls: ['./members.scss'],
})
export class MembersComponent implements OnInit {
  users: CombinedUser[] = [];
  filteredUsers: CombinedUser[] = [];
  loading = false;

  filter: 'all' | 'matrimonial' | 'membership' = 'all';

  constructor(private adminService: AdminService) {}

  async ngOnInit() {
    await this.loadAllUsers();
  }

  async loadAllUsers() {
    this.loading = true;
    try {
      const matrimonial = await this.adminService.getPendingMatrimonialUsers();
      const membership = await this.adminService.getPendingMembershipUsers();

      this.users = [
        ...matrimonial.map(u => ({ ...u, type: 'matrimonial' as const })),
        ...membership.map(u => ({ ...u, type: 'membership' as const })),
      ];

      this.applyFilter();
    } catch (err) {
      console.error('Error loading users:', err);
    } finally {
      this.loading = false;
    }
  }

  applyFilter() {
    if (this.filter === 'all') {
      this.filteredUsers = this.users;
    } else {
      this.filteredUsers = this.users.filter(u => u.type === this.filter);
    }
  }

  setFilter(type: 'all' | 'matrimonial' | 'membership') {
    this.filter = type;
    this.applyFilter();
  }

  async approve(user: CombinedUser) {
    try {
      await this.adminService.approveUser(user.id!, user.type);
      alert(`${user.type} user approved`);
      this.users = this.users.filter(u => u.id !== user.id);
      this.applyFilter();
    } catch (err) {
      console.error('Approval failed:', err);
      alert('Failed to approve user');
    }
  }

  async reject(user: CombinedUser) {
    try {
      await this.adminService.rejectUser(user.id!, user.type);
      alert(`${user.type} user rejected`);
      this.users = this.users.filter(u => u.id !== user.id);
      this.applyFilter();
    } catch (err) {
      console.error('Rejection failed:', err);
      alert('Failed to reject user');
    }
  }
}
