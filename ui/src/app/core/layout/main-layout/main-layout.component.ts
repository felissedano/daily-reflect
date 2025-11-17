import { Component } from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterLink } from '@angular/router';

import { MatSnackBar } from '@angular/material/snack-bar';
import { LanguageSwitcherComponent } from '../../lang/language-switcher/language-switcher.component';
import { MatToolbar } from '@angular/material/toolbar';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-main-layout',
  imports: [
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    RouterLink,
    LanguageSwitcherComponent,
    MatToolbar
],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.scss',
})
export class MainLayoutComponent {
  constructor(
    private authService: AuthService,
    private router: Router,
    private snackbar: MatSnackBar,
  ) {}

  logout() {
    this.authService.userLogout().subscribe({
      next: (res) => {
        if (res) {
          this.snackbar.open('Logout successful', undefined, {
            duration: 3000,
          });
          void this.router.navigate(['/auth/login']);
        } else {
          console.error('Logout failed for unknown reasons');
        }
      },
      error: (err) => console.error(err),
    });
  }

  protected readonly environment = environment;
}
