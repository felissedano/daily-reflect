import {Component, Input} from '@angular/core';
import {MatCard} from "@angular/material/card";
import {LanguageSwitcherComponent} from "../../lang/language-switcher/language-switcher.component";

@Component({
  selector: 'app-auth-layout',
  imports: [
    MatCard,
    LanguageSwitcherComponent,
  ],
  template: `
    <div class="auth-container"  >
      <h1>Daily Reflect</h1>
      <mat-card class="auth-card">
        <h1 class="auth-title">{{ title }}</h1>
        <ng-content></ng-content>
      </mat-card>
      <app-language-switcher/>
      <br>
      <br>
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
