import {ApplicationConfig, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideHttpClient, withFetch, withInterceptors, withXsrfConfiguration} from "@angular/common/http";
import {csrfInterceptor} from "./core/auth/interceptor/csrf.interceptor";
import {withCredentialsInterceptor} from "./core/auth/interceptor/withCredentials.interceptor";

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([csrfInterceptor, withCredentialsInterceptor]),
      withFetch(),
      withXsrfConfiguration({
        cookieName: 'XSRF-TOKEN',
        headerName: 'X-XSRF-TOKEN'
      }))
  ]
};
