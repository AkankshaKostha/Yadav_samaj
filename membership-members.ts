import { Component, OnInit } from '@angular/core';
import { AdminMemberService } from '../../../../core/admin-member';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface MembershipUser {
  id: number;
  fullName: string;
  phone: string;
  email: string;
  state?: string;
  membershipPlan?: string;
  profilePhoto?: string;
  referralCode?: string;
  status?: string;
}

@Component({
  selector: 'app-membership-members',
  standalone: true,
  templateUrl: './membership-members.html',
  styleUrls: ['./membership-members.scss'],
  imports: [CommonModule, FormsModule]
})
export class Membership_MembersComponent implements OnInit {
  members: MembershipUser[] = [];
  editingMember: MembershipUser | null = null;
  selectedImage: string | null = null;

  backendUrl = 'http://localhost:8081'; // ✅ your Spring Boot base URL

  constructor(private adminService: AdminMemberService) {}

  ngOnInit(): void {
    this.loadMembers();
  }

  loadMembers() {
    this.adminService.getAllMembershipUsers().subscribe({
      next: (data) => {
        // attach backend base path to photo URL if needed
     this.members = data.map((m) => ({
  ...m,
  profilePhoto: m.profilePhoto
    ? `${this.backendUrl}${m.profilePhoto}`
    : 'default-profile.png'
})) as MembershipUser[]; // ensure type is string

      },
      error: (err) => console.error('Failed to load members', err)
    });
  }

  editMember(member: MembershipUser) {
    this.editingMember = { ...member };
  }

  saveMember() {
    if (!this.editingMember) return;

    this.adminService.updateMembership(this.editingMember).subscribe({
      next: () => {
        alert('Member updated successfully');
        this.editingMember = null;
        this.loadMembers();
      },
      error: (err) => console.error('Update failed', err)
    });
  }

  deleteMember(id: number) {
    if (!confirm('Are you sure you want to delete this member?')) return;

    this.adminService.deleteMembership(id).subscribe({
      next: () => {
        alert('Member deleted successfully');
        this.loadMembers();
      },
      error: (err) => console.error('Delete failed', err)
    });
  }

  // ✅ When clicking the photo card
  openImage(photoUrl: string) {
    this.selectedImage = photoUrl;
  }

  closeImage() {
    this.selectedImage = null;
  }
}
