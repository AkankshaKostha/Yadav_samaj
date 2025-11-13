import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSearch } from './admin-search';

describe('AdminSearch', () => {
  let component: AdminSearch;
  let fixture: ComponentFixture<AdminSearch>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminSearch]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSearch);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
