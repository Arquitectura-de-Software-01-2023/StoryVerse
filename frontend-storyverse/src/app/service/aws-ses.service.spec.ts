import { TestBed } from '@angular/core/testing';

import { AwsSesService } from './aws-ses.service';

describe('AwsSesService', () => {
  let service: AwsSesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AwsSesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
