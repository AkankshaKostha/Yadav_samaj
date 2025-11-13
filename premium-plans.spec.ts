import { TestBed } from '@angular/core/testing';

import { PremiumPlans } from './premium-plans';

describe('PremiumPlans', () => {
  let service: PremiumPlans;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PremiumPlans);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
