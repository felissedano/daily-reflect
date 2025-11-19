import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../auth.service';
import { map } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService, _ as __ } from '@ngx-translate/core';

export const authGuard: CanActivateFn = (_route, _state) => {
  const router: Router = inject(Router);
  const authService: AuthService = inject(AuthService);
  const snackbar: MatSnackBar = inject(MatSnackBar);
  const translate: TranslateService = inject(TranslateService);

  return authService.checkAuthStatus().pipe(
    map((isLoggedIn: boolean) => {
      if (isLoggedIn) {
        return true;
      } else {
        void router.navigate(['/auth/login']);
        snackbar.open(translate.instant(__('auth.session-expired-or-not-logged-in.error')), undefined, {
          duration: 3000,
        });
        return false;
      }
    }),
  );
};
