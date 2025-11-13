import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeSections } from './home-sections';

describe('HomeSections', () => {
  let component: HomeSections;
  let fixture: ComponentFixture<HomeSections>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeSections]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomeSections);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
