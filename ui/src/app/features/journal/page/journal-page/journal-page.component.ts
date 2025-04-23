import {Component, OnInit} from '@angular/core';
import {MainLayoutComponent} from "../../../../core/layout/main-layout/main-layout.component";
import {AuthService} from "../../../../core/auth/auth.service";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatFormField} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslatePipe} from "@ngx-translate/core";
import {MatIcon} from "@angular/material/icon";
import {DatePipe} from "@angular/common";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {CalendarPopupComponent} from "../../calendar-popup/calendar-popup.component";
import {ActivatedRoute, Router} from "@angular/router";
import {JournalService} from "../../journal.service";
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-journal-page',
  imports: [MainLayoutComponent, MatButton, MatFormField, MatInput, TranslatePipe, MatIcon, DatePipe, MatIconButton, ReactiveFormsModule],
  templateUrl: './journal-page.component.html',
  styleUrl: './journal-page.component.scss'
})
export class JournalPageComponent implements OnInit {

  constructor(private authService: AuthService,
              private journalService: JournalService,
              private route: ActivatedRoute,
              private router: Router,
              private dialog: MatDialog) {
  }

  currentSelectedDate: Date = new Date();

  journalForm = new FormGroup({
    content: new FormControl<string>(""),
    labels: new FormControl<string[]>([])
  })

  ngOnInit(): void {

    // If no queryParam then display current local date
    if (this.route.snapshot.queryParamMap.keys.length === 0) {
      console.log("no params")
      this.currentSelectedDate = new Date();
    } else { //Get date from param and check if it's valid
      console.log("yes params")
      const yearString: string | null = this.route.snapshot.queryParamMap.get("year");
      const monthString: string | null = this.route.snapshot.queryParamMap.get("month");
      const dayString: string | null = this.route.snapshot.queryParamMap.get("day");

      if (yearString === null || monthString === null || dayString === null) {
        void this.router.navigate(["/404"]);
        return;
      }

      const isValidDate: boolean = this.isValidDate(yearString, monthString, dayString);

      if (isValidDate) {
        this.currentSelectedDate = new Date(Number(yearString), Number(monthString) + 1, Number(dayString));
      } else {
        void this.router.navigate(["/404"]);
        return;
      }
    }

    this.loadCurrentDateJournalData();

  }

  refreshSession(): void {
    this.authService.checkAuthStatus().subscribe({
      next: value => console.log(value),
      error: err => console.error(err)
    })
  }

  openCalendar(): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {currentSelectedDate: this.currentSelectedDate};
    dialogConfig.width = '400px';
    dialogConfig.hasBackdrop = true;

    const dialogRef = this.dialog.open(CalendarPopupComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((res: Date) => {
      console.log("after close")
      if (res) {
        console.log("after close success")
        void this.router.navigate(["/journal"], {queryParams: {year: res.getFullYear(), month: res.getMonth() + 1, day: res.getDate()}});
        this.currentSelectedDate = res;
        this.loadCurrentDateJournalData();
      }
    });
  }

  private isValidDate(yearString: string, monthString: string, dayString: string): boolean {
    if (Number.isNaN(yearString) || Number.isNaN(monthString) || Number.isNaN(dayString)) {
      return false;
    }

    const year = Number(yearString);
    const month = Number(monthString);
    const day = Number(dayString);

    if (year < 1900 || year > 9999) {
      return false;
    }

    if (month < 0 || month > 11) {
      return false;
    }

    if (day < 1 || day > 31) {
      return false;
    }

    return true

  }

  private loadCurrentDateJournalData(): void {

    this.journalService.getJournalOfDay(
      this.currentSelectedDate.getFullYear(),
      this.currentSelectedDate.getMonth(),
      this.currentSelectedDate.getDate()
    ).subscribe({
      next: value => {
        if (value) {
          this.journalForm.setValue({
            content: value.content,
            labels: value.labels
          });
        }
      }
    });
  }

}
