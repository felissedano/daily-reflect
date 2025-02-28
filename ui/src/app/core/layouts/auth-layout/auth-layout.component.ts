import {Component, Input} from '@angular/core';
import {MatCard} from "@angular/material/card";
import {MatToolbar} from "@angular/material/toolbar";

@Component({
  selector: 'app-auth-layout',
  imports: [
    MatCard,
    MatToolbar
  ],
  template: `
    <div class="auth-container"  >
      <h1>Daily Reflect</h1>
      <mat-card class="auth-card">
        <h1 class="auth-title">{{ title }}</h1>
        <ng-content></ng-content>
      </mat-card>
      <footer>All Right Reserved</footer>
    </div>  `,
  styles: `
    .auth-container {
      align-content: center; text-align: center; justify-content: center
    }

    .auth-card {
      padding-bottom: 30px;
    }
  `
})
export class AuthLayoutComponent {
  @Input() title: string = '';
}
