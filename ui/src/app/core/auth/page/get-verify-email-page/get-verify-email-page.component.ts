import { Component, OnInit } from '@angular/core';
import { AuthLayoutComponent } from '../../../layout/auth-layout/auth-layout.component';
import { MatButton } from '@angular/material/button';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../auth.service';
import { MatError } from '@angular/material/form-field';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NgIf } from '@angular/common';
import { GenericResponse } from '../../../model/generic-response';

@Component({
  selector: 'app-verify-email-page',
  imports: [AuthLayoutComponent, MatButton, MatError, NgIf],
  templateUrl: './get-verify-email-page.component.html',
  styleUrl: './get-verify-email-page.component.scss',
})
export class GetVerifyEmailPageComponent implements OnInit {
  constructor(
    private readonly route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private snackbar: MatSnackBar,
  ) {}

  userEmail: string | null = null;

  ngOnInit() {
    this.userEmail = this.route.snapshot.queryParamMap.get('email');
    if (this.userEmail === null) {
      void this.router.navigate(['/404']);
    }
  }

  errorMessage: string | null = null;

  resendEmail() {
    if (this.userEmail === '' || this.userEmail === null) {
      return;
    }
    this.authService.resendEmailVerification(this.userEmail).subscribe({
      next: (value) => {
        const response = value.body as GenericResponse;
        this.snackbar.open(response?.message ?? 'Success', 'close', {
          duration: 5000,
        });
      },
      error: (err) => console.log(err),
    });
  }

  goToLogin() {
    void this.router.navigate(['auth/login']);
  }
}
