import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatrimonialSections } from './matrimonial-sections';

describe('MatrimonialSections', () => {
  let component: MatrimonialSections;
  let fixture: ComponentFixture<MatrimonialSections>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MatrimonialSections]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MatrimonialSections);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
