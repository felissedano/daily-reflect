import {HttpInterceptorFn, HttpXsrfTokenExtractor} from '@angular/common/http';
import {inject} from "@angular/core";

export const csrfInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenExtractor = inject(HttpXsrfTokenExtractor);
  const token = tokenExtractor.getToken();

  if (token !== null && !req.headers.has('X-XSRF-TOKEN')) {
    req = req.clone({ headers: req.headers.set('X-XSRF-TOKEN', token) });
  }

  return next(req);
};
