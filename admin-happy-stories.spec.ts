import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HappyStories } from './happy-stories';

describe('HappyStories', () => {
  let component: HappyStories;
  let fixture: ComponentFixture<HappyStories>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HappyStories]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HappyStories);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
