import { TestBed } from '@angular/core/testing';

import { HappyStories } from './happy-stories';

describe('HappyStories', () => {
  let service: HappyStories;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HappyStories);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
