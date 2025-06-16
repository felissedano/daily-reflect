import {Component} from '@angular/core';
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

@Component({
  selector: 'app-journal-page',
  imports: [MainLayoutComponent, MatButton, MatFormField, MatInput, TranslatePipe, MatIcon, DatePipe, MatIconButton],
  templateUrl: './journal-page.component.html',
  styleUrl: './journal-page.component.scss'
})
export class JournalPageComponent {
  constructor(private authService: AuthService, private dialog: MatDialog) {
  }

  currentSelectedDate: Date = new Date();

  refreshSession() {
    this.authService.checkAuthStatus().subscribe({
      next: value => console.log(value),
      error: err => console.error(err)
    })
  }

  openCalendar(): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { currentSelectedDate: this.currentSelectedDate};
    dialogConfig.width = '400px';
    dialogConfig.hasBackdrop = true;

    const dialogRef = this.dialog.open(CalendarPopupComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(res => {
      if (res) {
        this.currentSelectedDate = res;
      }
    });
  }

}
