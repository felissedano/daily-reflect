import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'app-language-switcher',
  imports: [MatButton],
  template: `
    <button (click)="switchLanguage()" mat-raised-button>
      {{ currentLang === 'en' ? 'Français' : 'English' }}
    </button>
  `,
  styles: ``,
})
export class LanguageSwitcherComponent implements OnInit {
  currentLang: string = localStorage.getItem('locale') ?? 'en';

  constructor(private translateService: TranslateService) {}

  ngOnInit(): void {
    this.translateService.use(this.currentLang);
    this.translateService.setDefaultLang('en');
  }

  switchLanguage() {
    if (this.currentLang === 'en') {
      // Switch to french
      this.translateService.use('fr').subscribe({
        next: (_) => {
          localStorage.setItem('locale', 'fr');
          this.currentLang = 'fr';
        },
      });
    } else {
      // Switch to english
      this.translateService.use('en').subscribe({
        next: (_) => {
          localStorage.setItem('locale', 'en');
          this.currentLang = 'en';
        },
      });
    }
  }
}
