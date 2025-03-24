import { Component } from '@angular/core';
import {AuthLayoutComponent} from "../../../layout/auth-layout/auth-layout.component";
import {MatButton} from "@angular/material/button";
import {ActivatedRoute, Route, Router} from "@angular/router";
import {AuthService} from "../../auth.service";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-confirm-email-page',
  imports: [
    AuthLayoutComponent,
    MatButton,
    NgIf
  ],
  templateUrl: './confirm-email-page.component.html',
  styleUrl: './confirm-email-page.component.scss'
})
export class ConfirmEmailPageComponent {

  constructor(private route: ActivatedRoute, private router: Router, private authService: AuthService) {
  }

  confirmState: 'success' | 'failed' | 'na' = "na";

  userEmail: string | null = null;
  verificationCode: string | null = null;

  ngOnInit() {
    this.userEmail = this.route.snapshot.queryParamMap.get("email");
    this.verificationCode = this.route.snapshot.queryParamMap.get("code");
    if (this.userEmail === null || this.verificationCode === null) {
      void this.router.navigate(['/404']);
    }
  }

  protected readonly console = console;
}
