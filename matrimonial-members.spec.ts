import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatrimonialMembers } from './matrimonial-members';

describe('MatrimonialMembers', () => {
  let component: MatrimonialMembers;
  let fixture: ComponentFixture<MatrimonialMembers>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MatrimonialMembers]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MatrimonialMembers);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
