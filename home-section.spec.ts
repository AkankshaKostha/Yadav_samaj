import { TestBed } from '@angular/core/testing';

import { HomeSection } from './home-section';

describe('HomeSection', () => {
  let service: HomeSection;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HomeSection);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
