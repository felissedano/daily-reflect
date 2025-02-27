import { Routes } from '@angular/router';
import { JournalPageComponent } from './features/journal/pages/journal-page/journal-page.component';
import {LoginPageComponent} from "./core/auth/pages/login-page/login-page.component";

export const routes: Routes = [
  { path: 'auth/login', component: LoginPageComponent },
  { path: '', component: JournalPageComponent },
  { path: 'journal', component: JournalPageComponent },
];
