import { Routes } from '@angular/router';
import { JournalPageComponent } from './features/journal/page/journal-page/journal-page.component';
import {LoginPageComponent} from "./core/auth/page/login-page/login-page.component";
import {RegisterPageComponent} from "./core/auth/page/register-page/register-page.component";
import {authGuard} from "./core/auth/auth.guard";
import {PageNotFoundComponent} from "./static/page-not-found/page-not-found.component";
import {GetVerifyEmailPageComponent} from "./core/auth/page/get-verify-email-page/get-verify-email-page.component";
import {ConfirmEmailPageComponent} from "./core/auth/page/confirm-email-page/confirm-email-page.component";
import {ForgotPasswordPageComponent} from "./core/auth/page/forgot-password-page/forgot-password-page.component";
import {ResetPasswordPageComponent} from "./core/auth/page/reset-password-page/reset-password-page.component";

export const routes: Routes = [
  { path: 'auth', children: [
      { path: 'login', component: LoginPageComponent},
      { path: 'register', component: RegisterPageComponent},
      { path: 'verify-email', component: GetVerifyEmailPageComponent},
      { path: 'verify/user/email', component: ConfirmEmailPageComponent},
      { path: 'forgot-password', component: ForgotPasswordPageComponent},
      { path: 'reset-password', component: ResetPasswordPageComponent}
    ]
  },
  { path: 'journal', component: JournalPageComponent, canActivate: [authGuard] },
  { path: '', redirectTo: '/journal', pathMatch: "full"},
  { path: '**', component: PageNotFoundComponent}
];
