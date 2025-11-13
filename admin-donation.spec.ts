import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminDonation } from './admin-donation';

describe('AdminDonation', () => {
  let component: AdminDonation;
  let fixture: ComponentFixture<AdminDonation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminDonation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminDonation);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
