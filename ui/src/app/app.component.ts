import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from './core/layout/footer/footer.component';
import {TranslateService} from "@ngx-translate/core";

@Component({
    selector: 'app-root',
    imports: [RouterOutlet, FooterComponent],
    templateUrl: './app.component.html',
    styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'ui';

  constructor(private translateService: TranslateService) {
    this.translateService.addLangs(['en','fr'])
    this.translateService.setDefaultLang('en')
    this.translateService.use(translateService.getBrowserLang() === "fr" ? "fr" : "en");
  }
}
