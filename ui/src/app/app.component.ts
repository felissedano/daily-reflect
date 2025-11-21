import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { environment } from '../environments/environment';
import localFr from '@angular/common/locales/fr-CA';
import localEn from '@angular/common/locales/en-CA';
import { DatePipe, registerLocaleData } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
  providers: [DatePipe]
})
export class AppComponent {
  title = environment.appName;

  constructor(private translateService: TranslateService) {
    registerLocaleData(localFr, 'fr');
    registerLocaleData(localEn, 'en');
  }
}
