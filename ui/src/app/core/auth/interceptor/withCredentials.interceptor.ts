import { HttpInterceptorFn } from '@angular/common/http';

export const withCredentialsInterceptor: HttpInterceptorFn = (req, next) => {
  if (!req.withCredentials) {
    req = req.clone({ withCredentials: true });
  }
  return next(req);
};
