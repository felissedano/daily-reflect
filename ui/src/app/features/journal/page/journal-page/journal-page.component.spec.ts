import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JournalPageComponent } from './journal-page.component';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { TranslateModule } from '@ngx-translate/core';
import { RouterModule } from '@angular/router';
import { DatePipe } from '@angular/common';

describe('JournalPageComponent', () => {
  let component: JournalPageComponent;
  let fixture: ComponentFixture<JournalPageComponent>;

  const datePipeSpyObj = jasmine.createSpyObj('DatePipe', ['transform']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JournalPageComponent, TranslateModule.forRoot(), RouterModule.forRoot([])],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        {provide: DatePipe, useValue: datePipeSpyObj},

      ]
    }).compileComponents();

    fixture = TestBed.createComponent(JournalPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
