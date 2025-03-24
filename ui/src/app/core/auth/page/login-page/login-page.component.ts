import {Component} from '@angular/core';
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {AuthLayoutComponent} from "../../../layout/auth-layout/auth-layout.component";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatFormField, MatInput, MatSuffix} from "@angular/material/input";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatIcon} from "@angular/material/icon";
import {Router, RouterLink} from "@angular/router";
import {MatFormFieldModule} from "@angular/material/form-field";
import {NgIf} from "@angular/common";
import {AuthService} from "../../auth.service";
import {ProblemDetails} from "../../../model/problem-details";
import {HttpErrorResponse} from "@angular/common/http";
import {AuthProblemType} from "../../auth-problem-type";

@Component({
  selector: 'app-login-page',
  imports: [MatSlideToggleModule, AuthLayoutComponent, MatButton, MatInput, ReactiveFormsModule, MatFormFieldModule, MatIcon, MatIconButton, MatSuffix, RouterLink, NgIf],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss'
})
export class LoginPageComponent {

  constructor(private authService: AuthService, private router: Router) {
  }


  loginForm = new FormGroup({
    email: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  })

  loginErrorMessage: string | null = null;

  onSubmit() {
    console.log(this.loginForm.controls)
    console.log(this.loginForm.value)
    if (this.loginForm.invalid) {
      return
    }

    this.authService.userLogin(this.loginForm.value.email as string, this.loginForm.value.password as string).subscribe({
      next: (response) => {
        console.log("Success");
        void this.router.navigate(["/journal"]);
      },
      error: (err: HttpErrorResponse) => {
        const pd: ProblemDetails = err.error
        console.log(err.error);
        if (pd.type === AuthProblemType.ACCOUNT_DISABLED) {
          void this.router.navigate(["/auth/verify-email"], {queryParams: {email: this.loginForm.value.email}});
        }
        this.loginErrorMessage = pd.detail;
      }
    })

  }


  hidePwd = true;

  toggleHidePwd() {
    this.hidePwd = !this.hidePwd;
  }

  getInputType() {
    return this.hidePwd ? 'password' : 'text';
  }

  getVisibilityIcon() {
    return this.hidePwd ? 'visibility_off' : 'visibility';
  }
}
