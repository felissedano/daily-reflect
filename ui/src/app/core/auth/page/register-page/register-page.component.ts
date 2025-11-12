import { Component } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { AuthLayoutComponent } from '../../../layout/auth-layout/auth-layout.component';
import { MatButton } from '@angular/material/button';
import { MatInput } from '@angular/material/input';
import { NgIf } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../auth.service';
import {
  passwordValidatorArray,
  samePasswordValidator,
} from '../../auth-validators';

@Component({
  selector: 'app-register-page',
  imports: [
    FormsModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    AuthLayoutComponent,
    MatButton,
    MatInput,
    NgIf,
    RouterLink,
  ],
  templateUrl: './register-page.component.html',
  styleUrl: './register-page.component.scss',
})
export class RegisterPageComponent {
  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  registerForm = new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(4),
    ]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl(
      '',
      [Validators.required].concat(passwordValidatorArray),
    ),
    confirmPassword: new FormControl('', [
      Validators.required,
      samePasswordValidator,
    ]),
  });

  onSubmit = () => {
    if (this.registerForm.invalid) {
      return;
    }

    const email = this.registerForm.value.email as string;
    const password = this.registerForm.value.password as string;
    const username = this.registerForm.value.username as string;

    this.authService.registerUser({ email, password, username }).subscribe({
      next: (value) => {
        console.log(value.body);
        void this.router.navigate(['auth/verify-email'], {
          queryParams: { email: email },
        });
      },
      error: (err) => (this.errorMessage = err.error.detail),
    });
  };

  errorMessage: null | string = null;
}
