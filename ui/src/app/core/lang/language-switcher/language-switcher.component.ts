import { Component } from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-language-switcher',
  imports: [
    MatButton
  ],
  template: `
    <button (click)="switchLanguage()" mat-raised-button>{{currentLang === 'en' ? "Francais" : "English"}}</button>
  `,
  styles: `
  `
})
export class LanguageSwitcherComponent {
  currentLang = "en";

  constructor(private translateService: TranslateService) {

  }

  switchLanguage() {
    if (this.currentLang === "en") {
      this.translateService.use('fr');
      this.currentLang = "fr"
    } else {
      this.translateService.use('en');
      this.currentLang = "en"
    }
  }
}
