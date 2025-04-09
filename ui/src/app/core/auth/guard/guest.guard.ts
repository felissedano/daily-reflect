import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../auth.service";
import {map} from "rxjs";

export const guestGuard: CanActivateFn = (route, state) => {
  const router: Router = inject(Router);
  const authService: AuthService = inject(AuthService);

  return authService.checkAuthStatus().pipe(
    map((isLoggedIn: boolean) => {
      if (!isLoggedIn) {
        return true;
      } else {
        void router.navigate(['/journal']);
        return false;
      }
    })
  );
};
