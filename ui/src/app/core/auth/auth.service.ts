import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { from, map, Observable, switchMap } from 'rxjs';
import { CryptoService } from '../crypto/crypto.service';
import { EncryptedDekProperties } from '../crypto/encrypted-dek-properties';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private API_URL = environment.baseUrl;

  constructor(
    private httpClient: HttpClient,
    private cryptoService: CryptoService,
  ) {}

  // On login success, add event listen to see if user interacted with the app, if yes, then extent session duration periodically
  userLogin(email: string, password: string) {
    const headers = new HttpHeaders().set('content-type', 'application/json');
    return this.httpClient
      .post(
        this.API_URL + 'api/auth/login',
        { email: email, password: password },
        {
          headers: headers,
          observe: 'response',
        },
      )
      .pipe(
        switchMap((_) =>
          this.httpClient.get<EncryptedDekProperties>(
            this.API_URL + 'api/user/dek',
            { observe: 'body' },
          ),
        ),
        switchMap((dekProperties) =>
          from(
            this.cryptoService.decryptAndSetDek(
              password,
              dekProperties.encryptedDek,
              dekProperties.salt,
              dekProperties.iv,
            ),
          ),
        ),
      );
  }

  userLogout() {
    return this.httpClient
      .post<string>(
        this.API_URL + 'api/auth/logout',
        {},
        {
          observe: 'response',
        },
      )
      .pipe(map((res) => res.ok));
  }

  checkAuthStatus() {
    return this.httpClient
      .get<boolean>(this.API_URL + 'api/auth/is-auth', {
        observe: 'response',
      })
      .pipe(map((res) => (res.body === null ? false : res.body)));
  }

  registerUser(user: {
    email: string;
    password: string;
    username: string;
  }): Observable<HttpResponse<object>> {
    const headers = new HttpHeaders().set('content-type', 'application/json');
    const dekProperties$ = from(
      this.cryptoService.generateNewDekEncrypted(user.password),
    );
    console.log('Key created, about to create user');
    return dekProperties$.pipe(
      map((dek) => ({ ...user, ...dek })),
      switchMap((userAndKey) =>
        this.httpClient.post(this.API_URL + 'api/auth/register', userAndKey, {
          observe: 'response',
          headers: headers,
        }),
      ),
    );
  }

  resendEmailVerification(email: string) {
    return this.httpClient.post(
      this.API_URL + 'api/auth/get-verification-token',
      {},
      {
        observe: 'response',
        params: { email: email },
      },
    );
  }

  confirmEmail(email: string, code: string) {
    return this.httpClient.post(
      this.API_URL + 'api/auth/verify-email',
      {},
      {
        observe: 'response',
        params: { email: email, code: code },
      },
    );
  }

  sendPasswordResetLink(email: string) {
    return this.httpClient.post(
      this.API_URL + 'api/auth/get-reset-password-link',
      {},
      {
        observe: 'response',
        params: { email: email },
      },
    );
  }

  resetPassword(passwordResetInfo: {
    email: string;
    password: string;
    token: string;
  }) {
    return this.httpClient.post(
      this.API_URL + 'api/auth/reset-password',
      passwordResetInfo,
      { observe: 'response' },
    );
  }
}
