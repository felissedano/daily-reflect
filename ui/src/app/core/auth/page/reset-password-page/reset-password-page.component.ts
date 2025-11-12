import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ProblemDetails } from '../../../model/problem-details';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  passwordValidatorArray,
  samePasswordValidator,
} from '../../auth-validators';
import { AuthLayoutComponent } from '../../../layout/auth-layout/auth-layout.component';
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { NgIf } from '@angular/common';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'app-reset-password-page',
  imports: [
    AuthLayoutComponent,
    FormsModule,
    ReactiveFormsModule,
    MatFormField,
    MatError,
    MatInput,
    MatLabel,
    NgIf,
    MatButton,
    RouterLink,
  ],
  templateUrl: './reset-password-page.component.html',
  styleUrl: './reset-password-page.component.scss',
})
export class ResetPasswordPageComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
  ) {}

  confirmState: 'success' | 'na' = 'na';
  errorMessage: string | null = null;

  userEmail: string = '';
  verificationCode: string = '';

  resetPasswordForm = new FormGroup({
    password: new FormControl('', passwordValidatorArray),
    confirmPassword: new FormControl('', samePasswordValidator),
  });

  ngOnInit() {
    this.verificationCode = this.route.snapshot.queryParamMap.get('code') ?? '';
    this.userEmail = this.route.snapshot.queryParamMap.get('email') ?? '';
    if (this.userEmail === '' || this.verificationCode === '') {
      void this.router.navigate(['/404']);
    }
  }

  resetPassword() {
    if (!this.resetPasswordForm.valid) {
      return;
    }

    this.authService
      .resetPassword({
        email: this.userEmail,
        password: this.resetPasswordForm.value.password as string,
        token: this.verificationCode,
      })
      .subscribe({
        next: (value) => (this.confirmState = 'success'),
        error: (err) => {
          const pd = err.error as ProblemDetails;
          this.errorMessage = pd.detail;
        },
      });
  }

  goToLoginPage() {
    void this.router.navigate(['auth/login']);
  }
}
