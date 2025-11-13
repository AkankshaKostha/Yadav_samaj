import { TestBed } from '@angular/core/testing';

import { MatrimonialUser } from './matrimonial-user';

describe('MatrimonialUser', () => {
  let service: MatrimonialUser;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MatrimonialUser);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
