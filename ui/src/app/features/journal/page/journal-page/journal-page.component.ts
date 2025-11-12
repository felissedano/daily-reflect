import {
  Component,
  computed,
  inject,
  OnInit,
  signal,
  Signal,
  WritableSignal,
} from '@angular/core';
import { MainLayoutComponent } from '../../../../core/layout/main-layout/main-layout.component';
import { AuthService } from '../../../../core/auth/auth.service';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatFormField } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { TranslatePipe } from '@ngx-translate/core';
import { MatIcon } from '@angular/material/icon';
import { DatePipe } from '@angular/common';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { CalendarPopupComponent } from '../../calendar-popup/calendar-popup.component';
import { ActivatedRoute, Router } from '@angular/router';
import { JournalService } from '../../journal.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Journal } from '../../journal.model';
import { HttpErrorResponse } from '@angular/common/http';
import { ProblemDetails } from '../../../../core/model/problem-details';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of } from 'rxjs';

@Component({
  selector: 'app-journal-page',
  imports: [
    MainLayoutComponent,
    MatButton,
    MatFormField,
    MatInput,
    TranslatePipe,
    MatIcon,
    DatePipe,
    MatIconButton,
    ReactiveFormsModule,
  ],
  templateUrl: './journal-page.component.html',
  styleUrl: './journal-page.component.scss',
})
export class JournalPageComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private journalService: JournalService,
    private route: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog,
    private matSnackBar: MatSnackBar,
  ) {}

  currentSelectedDate: Date = new Date();

  journalForm = new FormGroup({
    content: new FormControl<string>(''),
    tags: new FormControl<string[]>([]),
  });

  ngOnInit(): void {
    // If no queryParam then display current local date
    if (this.route.snapshot.queryParamMap.keys.length === 0) {
      this.currentSelectedDate = new Date();
    } else {
      //Get date from param and check if it's valid
      const yearString: string | null =
        this.route.snapshot.queryParamMap.get('year');
      const monthString: string | null =
        this.route.snapshot.queryParamMap.get('month');
      const dateString: string | null =
        this.route.snapshot.queryParamMap.get('date');

      if (yearString === null || monthString === null || dateString === null) {
        void this.router.navigate(['/404']);
        return;
      }

      const isValidDate: boolean = this.isValidDate(
        yearString,
        monthString,
        dateString,
      );

      if (isValidDate) {
        this.currentSelectedDate = new Date(
          Number(yearString),
          Number(monthString) - 1, // Month is 0 indexed!
          Number(dateString),
        );
      } else {
        void this.router.navigate(['/404']);
        return;
      }
    }

    this.loadCurrentDateJournalData();
  }

  refreshSession(): void {
    this.authService.checkAuthStatus().subscribe({
      next: (value) => console.log(value),
      error: (err) => console.error(err),
    });
  }

  openCalendar(): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { currentSelectedDate: this.currentSelectedDate };
    dialogConfig.width = '400px';
    dialogConfig.minHeight = '500px';
    dialogConfig.hasBackdrop = true;

    const dialogRef = this.dialog.open(CalendarPopupComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((res: Date) => {
      if (res) {
        this.changeCurrentSelectedDate(res);
      }
    });
  }

  changeDateByOneDay(direction: 'prev' | 'next'): void {
    const date: Date = new Date(this.currentSelectedDate);
    if (direction === 'prev') {
      date.setDate(date.getDate() - 1);
      this.changeCurrentSelectedDate(date);
      // this.currentSelectedDate.update()
    } else {
      date.setDate(date.getDate() + 1);
      this.changeCurrentSelectedDate(date);
    }
  }

  saveJournal() {
    const journalToSave: Journal = {
      content: this.journalForm.value.content ?? '',
      tags: this.journalForm.value.tags ?? [],
      date: this.currentSelectedDate,
    };
    this.journalService.saveJournal(journalToSave).subscribe({
      next: (_) =>
        this.matSnackBar.open('journal.journalSaved', undefined, {
          duration: 2000,
        }),
      error: (_) => this.matSnackBar.open('error.journal.journalSaveFailed'),
    });
  }

  private isValidDate(
    yearString: string,
    monthString: string,
    dayString: string,
  ): boolean {
    if (
      Number.isNaN(yearString) ||
      Number.isNaN(monthString) ||
      Number.isNaN(dayString)
    ) {
      return false;
    }

    const year = Number(yearString);
    const month = Number(monthString);
    const day = Number(dayString);

    if (year < 1900 || year > 9999) {
      return false;
    }

    if (month < 1 || month > 12) {
      return false;
    }

    if (day < 1 || day > 31) {
      return false;
    }

    return true;
  }

  private loadCurrentDateJournalData(): void {
    this.journalService.getJournalByDate(this.currentSelectedDate).subscribe({
      next: (journal) => {
        console.log('journal' + journal);
        if (journal) {
          this.journalForm.setValue({
            content: journal.content,
            tags: journal.tags,
          });
          // User does not have a journal created yet
        } else {
          this.journalForm.setValue({
            content: '',
            tags: [],
          });
        }
      },
      error: (error: HttpErrorResponse) => {
        const status: number = error.status;
        console.log('error' + error);
        this.matSnackBar.open(
          'unknown error occured (status code ' + status + ')',
        );
        this.journalForm.setValue({
          content: '',
          tags: [],
        });
      },
    });
  }

  private changeCurrentSelectedDate(res: Date) {
    void this.router.navigate(['/journal'], {
      queryParams: {
        year: res.getFullYear(),
        month: res.getMonth() + 1,
        date: res.getDate(),
      },
    });
    this.currentSelectedDate = res;
    this.loadCurrentDateJournalData();
  }
}
