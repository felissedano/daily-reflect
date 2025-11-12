import { Component, Inject, OnInit } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
} from '@angular/material/dialog';
import { DatePipe, NgForOf } from '@angular/common';
import { Journal } from '../journal.model';
import { JournalService } from '../journal.service';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { stringfyDate } from '../../../shared/util/dateUtil';
import { TranslatePipe } from '@ngx-translate/core';

interface CalendarDay {
  date: Date;
  isCurrentMonth: boolean;
}

@Component({
  selector: 'app-calendar-popup',
  imports: [
    MatDialogContent,
    DatePipe,
    MatDialogActions,
    MatDialogClose,
    MatButton,
    MatIconButton,
    MatIcon,
    NgForOf,
    TranslatePipe,
  ],
  templateUrl: './calendar-popup.component.html',
  styleUrl: './calendar-popup.component.scss',
})
export class CalendarPopupComponent implements OnInit {
  currentMonth: Date;
  calendarDays: CalendarDay[] = [];
  currentSelectedDate: Date;
  weekDayNames = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  journalByDate: { [key: string]: Journal } = {};

  constructor(
    private dialogRef: MatDialogRef<CalendarPopupComponent>,
    @Inject(MAT_DIALOG_DATA) private data: { currentSelectedDate: Date },
    private journalService: JournalService,
  ) {
    this.currentMonth = new Date(data.currentSelectedDate);
    this.currentSelectedDate = new Date(data.currentSelectedDate);
  }

  ngOnInit(): void {
    this.generateCalendar();
    this.loadJournalOfMonth();
  }

  generateCalendar(): void {
    this.calendarDays = [];

    const firstDayOfMonth = new Date(
      this.currentMonth.getFullYear(),
      this.currentMonth.getMonth(),
      1,
    );
    const lastDayOfMonth = new Date(
      this.currentMonth.getFullYear(),
      this.currentMonth.getMonth() + 1,
      0,
    );

    // If first day is not sunday, then need to fill date from previous month until the start of current month
    const startingWeekDay = firstDayOfMonth.getDay();
    for (let i = 0; i < startingWeekDay; i++) {
      const date = new Date(firstDayOfMonth);
      date.setDate(date.getDate() - (startingWeekDay - i));
      this.calendarDays.push({ date, isCurrentMonth: false });
    }

    // Days of current month
    for (let i = 1; i <= lastDayOfMonth.getDate(); i++) {
      const date = new Date(
        this.currentMonth.getFullYear(),
        this.currentMonth.getMonth(),
        i,
      );
      this.calendarDays.push({ date, isCurrentMonth: true });
    }

    // If last day of current month is not saturday, then fill date from next month until all blocks is filled
    const endingWeekDay = lastDayOfMonth.getDay();
    for (let i = 1; i < 7 - endingWeekDay; i++) {
      const date = new Date(lastDayOfMonth);
      date.setDate(date.getDate() + i);
      this.calendarDays.push({ date, isCurrentMonth: false });
    }
  }

  previousMonth(): void {
    this.currentMonth = new Date(
      this.currentMonth.getFullYear(),
      this.currentMonth.getMonth() - 1,
      1,
    );
    this.generateCalendar();
    this.loadJournalOfMonth();
  }

  nextMonth(): void {
    this.currentMonth = new Date(
      this.currentMonth.getFullYear(),
      this.currentMonth.getMonth() + 1,
      1,
    );
    this.generateCalendar();
    this.loadJournalOfMonth();
  }

  selectDay(date: Date): void {
    if (date.getMonth() !== this.currentMonth.getMonth()) {
      return;
    }

    this.currentSelectedDate = date;
  }

  isSelectedDay(date: Date): boolean {
    return this.isSameDate(date, this.currentSelectedDate);
  }

  hasJournal(date: Date): boolean {
    const stringDate = stringfyDate(date);
    return this.journalByDate[stringDate] !== undefined;
  }

  private loadJournalOfMonth(): void {
    const year = this.currentMonth.getFullYear();
    const month = this.currentMonth.getMonth();
    this.journalService
      .getJournalByYearMonth(year, month)
      .subscribe((journals) => {
        this.journalByDate = {};
        journals.forEach((journal) => {
          const dateString: string = stringfyDate(journal.date);
          this.journalByDate[dateString] = journal;
        });
      });
  }

  private isSameDate(date: Date, selectedDate: Date) {
    return (
      date.getFullYear() === selectedDate.getFullYear() &&
      date.getMonth() === selectedDate.getMonth() &&
      date.getDate() === selectedDate.getDate()
    );
  }
}
