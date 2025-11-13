import { TestBed } from '@angular/core/testing';

import { Support } from './support';

describe('Support', () => {
  let service: Support;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Support);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
