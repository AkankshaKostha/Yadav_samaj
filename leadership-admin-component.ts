import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HomeSectionService } from '../../../core/home-section';
import { HomeSection } from '../../../core/models';

@Component({
  selector: 'app-leadership-admin',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './leadership-admin-component.html',
  styleUrls: ['./leadership-admin-component.scss']
})
export class LeadershipAdminComponent implements OnInit {

  leadershipList: HomeSection[] = [];
  leadershipForm: FormGroup;
  selectedFile: File | null = null;
  editId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private sectionService: HomeSectionService
  ) {
    this.leadershipForm = this.fb.group({
      title: ['', Validators.required],
      description: [''],
      route: [''],
      image: [null]
    });
  }

  ngOnInit(): void {
    this.loadLeadership();
  }

  loadLeadership() {
    this.sectionService.getLeadership().subscribe({
      next: data => this.leadershipList = data,
      error: err => console.error('Failed to load leadership', err)
    });
  }

  onFileChange(event: any) {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  submit() {
    const formData = new FormData();
    formData.append('title', this.leadershipForm.value.title);
    formData.append('description', this.leadershipForm.value.description);
    formData.append('route', this.leadershipForm.value.route);
    if (this.selectedFile) formData.append('image', this.selectedFile);

    if (this.editId) {
      this.sectionService.updateLeadership(this.editId, formData).subscribe({
        next: res => { this.resetForm(); this.loadLeadership(); },
        error: err => console.error('Update failed', err)
      });
    } else {
      this.sectionService.addLeadership(formData).subscribe({
        next: res => { this.resetForm(); this.loadLeadership(); },
        error: err => console.error('Add failed', err)
      });
    }
  }

  edit(section: HomeSection) {
    this.editId = section.id!;
    this.leadershipForm.patchValue({
      title: section.title,
      description: section.description,
      route: section.route
    });
  }

  delete(id: number) {
    if (confirm('Are you sure to delete?')) {
      this.sectionService.deleteLeadership(id).subscribe({
        next: res => this.loadLeadership(),
        error: err => console.error('Delete failed', err)
      });
    }
  }

  resetForm() {
    this.editId = null;
    this.selectedFile = null;
    this.leadershipForm.reset();
  }

  getImageUrl(section: HomeSection) {
    return section.imageUrl ? `http://localhost:8081${section.imageUrl}` : 'assets/images/default-leader.jpg';
  }
}
