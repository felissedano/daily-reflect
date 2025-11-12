import { Component } from '@angular/core';
import { AuthLayoutComponent } from '../../../layout/auth-layout/auth-layout.component';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { NgIf } from '@angular/common';
import { MatButton } from '@angular/material/button';
import { AuthService } from '../../auth.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-forgot-password-page',
  imports: [
    AuthLayoutComponent,
    ReactiveFormsModule,
    MatLabel,
    MatInput,
    MatError,
    NgIf,
    MatFormField,
    MatButton,
    RouterLink,
  ],
  templateUrl: './forgot-password-page.component.html',
  styleUrl: './forgot-password-page.component.scss',
})
export class ForgotPasswordPageComponent {
  constructor(private authService: AuthService) {}

  actionState: 'not-sent' | 'sent' = 'not-sent';

  errorMsg: string | null = null;

  forgotPasswordForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
  });

  onSubmit() {
    if (!this.forgotPasswordForm.valid) {
      return;
    }
    this.authService
      .sendPasswordResetLink(this.forgotPasswordForm.value.email as string)
      .subscribe({
        next: (value) => (this.actionState = 'sent'),
        error: (err) => {
          console.error(err);
          this.errorMsg = err.error.detail;
        },
      });
  }
}
