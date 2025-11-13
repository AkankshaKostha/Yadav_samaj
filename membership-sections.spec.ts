import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MembershipSections } from './membership-sections';

describe('MembershipSections', () => {
  let component: MembershipSections;
  let fixture: ComponentFixture<MembershipSections>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MembershipSections]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MembershipSections);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
