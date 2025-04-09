import { Component } from '@angular/core';
import {MainLayoutComponent} from "../../../../core/layout/main-layout/main-layout.component";
import {AuthService} from "../../../../core/auth/auth.service";
import {MatButton} from "@angular/material/button";

@Component({
    selector: 'app-journal-page',
  imports: [MainLayoutComponent, MatButton],
    templateUrl: './journal-page.component.html',
    styleUrl: './journal-page.component.scss'
})
export class JournalPageComponent {
  constructor(private authService: AuthService) {
  }


  refreshSession() {
    this.authService.checkAuthStatus().subscribe({
      next: value => console.log(value),
      error: err => console.error(err)
    })
  }

}
