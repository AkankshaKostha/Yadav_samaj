import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeadershipAdminComponent } from './leadership-admin-component';

describe('LeadershipAdminComponent', () => {
  let component: LeadershipAdminComponent;
  let fixture: ComponentFixture<LeadershipAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LeadershipAdminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LeadershipAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
