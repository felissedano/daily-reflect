import {Routes} from "@angular/router";
import {LoginPageComponent} from "./page/login-page/login-page.component";
import {RegisterPageComponent} from "./page/register-page/register-page.component";
import {GetVerifyEmailPageComponent} from "./page/get-verify-email-page/get-verify-email-page.component";
import {ConfirmEmailPageComponent} from "./page/confirm-email-page/confirm-email-page.component";
import {ForgotPasswordPageComponent} from "./page/forgot-password-page/forgot-password-page.component";
import {ResetPasswordPageComponent} from "./page/reset-password-page/reset-password-page.component";

export const authRoutes: Routes = [
  {
    path: "",
    children: [
      { path: 'login', component: LoginPageComponent},
      { path: 'register', component: RegisterPageComponent},
      { path: 'verify-email', component: GetVerifyEmailPageComponent},
      { path: 'verify/user/email', component: ConfirmEmailPageComponent},
      { path: 'forgot-password', component: ForgotPasswordPageComponent},
      { path: 'reset-password', component: ResetPasswordPageComponent}
    ]
  }
]
