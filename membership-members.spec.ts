import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MembershipMembers } from './membership-members';

describe('MembershipMembers', () => {
  let component: MembershipMembers;
  let fixture: ComponentFixture<MembershipMembers>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MembershipMembers]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MembershipMembers);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
