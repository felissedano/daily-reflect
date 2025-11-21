import { Component, OnInit } from '@angular/core';
import { MainLayoutComponent } from '../../../../core/layout/main-layout/main-layout.component';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatFormField, MatFormFieldModule } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { TranslatePipe, TranslateService, _ as __ } from '@ngx-translate/core';
import { MatIcon } from '@angular/material/icon';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { CalendarPopupComponent } from '../../calendar-popup/calendar-popup.component';
import { ActivatedRoute, Router } from '@angular/router';
import { JournalService } from '../../journal.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { JournalDto } from '../../journal.model';
import { HttpErrorResponse } from '@angular/common/http';
import {
  checkIsValidDate,
  stringfyDate,
} from '../../../../shared/util/dateUtil';
import { MatSnackBar } from '@angular/material/snack-bar';
import {
  MatChipEditedEvent,
  MatChipInputEvent,
  MatChipsModule,
} from '@angular/material/chips';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { LocalizedDatePipe } from '../../../../shared/localized-date.pipe';

@Component({
  selector: 'app-journal-page',
  imports: [
    MainLayoutComponent,
    MatButton,
    MatFormField,
    MatInput,
    TranslatePipe,
    MatIcon,
    LocalizedDatePipe,
    MatIconButton,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatChipsModule,
  ],
  templateUrl: './journal-page.component.html',
  styleUrl: './journal-page.component.scss',
})
export class JournalPageComponent implements OnInit {
  constructor(
    private journalService: JournalService,
    private route: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog,
    private matSnackBar: MatSnackBar,
    private translate: TranslateService,
  ) {}

  currentSelectedDate: Date = new Date();

  journalForm = new FormGroup({
    content: new FormControl<string>(''),
    tags: new FormControl<string[]>([]),
  });

  readonly MAT_CHIP_SEPARATOR = [ENTER, COMMA] as const;

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

      const isValidDate: boolean = checkIsValidDate(
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

  editTag(index: number, event: MatChipEditedEvent) {
    this.journalForm.value.tags![index] = event.value;
  }

  removeTag(index: number) {
    this.journalForm.value.tags?.splice(index, 1);
  }

  addTag(event: MatChipInputEvent) {
    this.journalForm.value.tags?.push(event.value);
    event.chipInput!.clear();
  }

  saveJournal() {
    const journalToSave: JournalDto = {
      content: this.journalForm.value.content ?? '',
      tags: this.journalForm.value.tags ?? [],
      date: stringfyDate(this.currentSelectedDate),
    };
    this.journalService.saveJournal(journalToSave).subscribe({
      next: (_) =>
        this.matSnackBar.open(
          this.translate.instant(__('journal.journalSaved.notification')),
          undefined,
          {
            duration: 2000,
          },
        ),
      error: (_) =>
        this.matSnackBar.open(
          this.translate.instant(__('journal.journalSaveFailed.error')),
          undefined,
          {
            duration: 2000,
          },
        ),
    });
  }

  private loadCurrentDateJournalData(): void {
    this.journalService.getJournalByDate(this.currentSelectedDate).subscribe({
      next: (journal) => {
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
        // Getting unauthorized code, means session expired
        if (status === 401) {
          void this.router.navigate(['auth/login']);
          this.matSnackBar.open(
            this.translate.instant(
              __('auth.session-expired-or-not-logged-in.error'),
            ),
            undefined,
            {
              duration: 2000,
            },
          );
        } else {
          this.matSnackBar.open(
            'unknown error occurred (status code ' + status + ')',
            undefined,
            { duration: 2000 },
          );
          this.journalForm.setValue({
            content: '',
            tags: [],
          });
        }
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
