import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { TranslateModule } from '@ngx-translate/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { CalendarPopupComponent } from './calendar-popup.component';
import { JournalService } from '../journal.service';
import { of } from 'rxjs';

describe('CalendarPopupComponent', () => {
  let component: CalendarPopupComponent;
  let fixture: ComponentFixture<CalendarPopupComponent>;

  const journalServiceMock = jasmine.createSpyObj('JournalService', [
    'getJournalByYearMonth',
  ]);
  journalServiceMock.getJournalByYearMonth.and.returnValue(of([]));

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CalendarPopupComponent, TranslateModule.forRoot()],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: MAT_DIALOG_DATA, useValue: { currentSelectedDate: new Date()} },
        { provide: MatDialogRef, useValue: {} },
        { provide: JournalService, useValue: journalServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CalendarPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
