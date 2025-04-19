import {Component} from '@angular/core';
import {MainLayoutComponent} from "../../../../core/layout/main-layout/main-layout.component";
import {AuthService} from "../../../../core/auth/auth.service";
import {MatButton} from "@angular/material/button";
import {MatFormField} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslatePipe} from "@ngx-translate/core";
import {MatIcon} from "@angular/material/icon";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-journal-page',
  imports: [MainLayoutComponent, MatButton, MatFormField, MatInput, TranslatePipe, MatIcon, DatePipe],
  templateUrl: './journal-page.component.html',
  styleUrl: './journal-page.component.scss'
})
export class JournalPageComponent {
  constructor(private authService: AuthService) {
  }

  date: Date = new Date();

  refreshSession() {
    this.authService.checkAuthStatus().subscribe({
      next: value => console.log(value),
      error: err => console.error(err)
    })
  }

}
