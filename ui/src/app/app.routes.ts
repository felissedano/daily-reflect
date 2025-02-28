import { Routes } from '@angular/router';
import { JournalPageComponent } from './features/journal/pages/journal-page/journal-page.component';
import {LoginPageComponent} from "./core/auth/pages/login-page/login-page.component";
import {RegisterPageComponent} from "./core/auth/pages/register-page/register-page.component";
import {authGuard} from "./core/auth/auth.guard";

export const routes: Routes = [
  { path: 'auth', children: [
      { path: 'login', component: LoginPageComponent},
      { path: 'register', component: RegisterPageComponent}
    ]
  },
  { path: '', component: JournalPageComponent, canActivate: [authGuard] },
  { path: 'journal', component: JournalPageComponent },
];
