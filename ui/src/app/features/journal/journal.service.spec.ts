import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { JournalService } from './journal.service';

describe('JournalService', () => {
  let service: JournalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ]
    });
    service = TestBed.inject(JournalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
