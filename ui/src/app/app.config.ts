import {
  ApplicationConfig,
  importProvidersFrom,
  provideZoneChangeDetection,
} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {
  HttpClient,
  provideHttpClient,
  withFetch,
  withInterceptors,
  withXsrfConfiguration,
} from '@angular/common/http';
import { csrfInterceptor } from './core/auth/interceptor/csrf.interceptor';
import { withCredentialsInterceptor } from './core/auth/interceptor/withCredentials.interceptor';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

const translateHttpLoaderFactory: (http: HttpClient) => TranslateHttpLoader = (
  http: HttpClient,
) => new TranslateHttpLoader(http, './i18n/', '.json');

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([csrfInterceptor, withCredentialsInterceptor]),
      withFetch(),
      withXsrfConfiguration({
        cookieName: 'XSRF-TOKEN',
        headerName: 'X-XSRF-TOKEN',
      }),
    ),
    importProvidersFrom([
      TranslateModule.forRoot({
        loader: {
          provide: TranslateLoader,
          useFactory: translateHttpLoaderFactory,
          deps: [HttpClient],
        },
      }),
    ]),
  ],
};
