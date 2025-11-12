import { Component, OnInit } from '@angular/core';
import { AuthLayoutComponent } from '../../../layout/auth-layout/auth-layout.component';
import { MatButton } from '@angular/material/button';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../auth.service';
import { NgIf } from '@angular/common';
import { ProblemDetails } from '../../../model/problem-details';

@Component({
  selector: 'app-confirm-email-page',
  imports: [AuthLayoutComponent, MatButton, NgIf],
  templateUrl: './confirm-email-page.component.html',
  styleUrl: './confirm-email-page.component.scss',
})
export class ConfirmEmailPageComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
  ) {}

  confirmState: 'success' | 'failed' | 'na' = 'na';
  errorMessage: string | null = null;

  userEmail: string = '';
  verificationCode: string = '';

  ngOnInit() {
    this.userEmail = this.route.snapshot.queryParamMap.get('email') ?? '';
    this.verificationCode = this.route.snapshot.queryParamMap.get('code') ?? '';
    if (this.userEmail === '' || this.verificationCode === '') {
      void this.router.navigate(['/404']);
    }
  }

  confirmEmail() {
    this.authService
      .confirmEmail(this.userEmail, this.verificationCode)
      .subscribe({
        next: (value) => (this.confirmState = 'success'),
        error: (err) => {
          const pd = err.error as ProblemDetails;
          this.confirmState = 'failed';
          this.errorMessage = pd.detail;
        },
      });
  }

  goToLoginPage() {
    void this.router.navigate(['auth/login']);
  }
}
