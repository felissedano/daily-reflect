import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {map} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private API_URL = environment.baseUrl;


  constructor(private httpClient: HttpClient) {
  }

  // On login success, add event listen to see if user interacted with the app, if yes, then extent session duration periodically
  userLogin(email: string, password: string) {
    const headers = new HttpHeaders()
      .set('content-type', 'application/json');
    return this.httpClient.post(this.API_URL + "api/auth/login", {email: email, password: password}, {
      headers: headers,
      observe: "response",
      withCredentials: true
    });
  }

  userLogout() {
    return this.httpClient.delete<string>(this.API_URL + "api/auth/logout", {
      withCredentials: true,
      observe: 'response'
    }).pipe(
      map(res => res.ok)
    );
  }

  checkAuthStatus() {
    return this.httpClient.get<boolean>(this.API_URL + "api/auth/is-auth", {
      withCredentials: true,
      observe: 'response'
    }).pipe(map((res) => res.body === null ? false : res.body))
  }

  registerUser(user: { email: string, password: string, username: string }) {
    const headers = new HttpHeaders()
      .set('content-type', 'application/json');
    return this.httpClient.post(this.API_URL + "api/auth/register", user, {
      observe: 'response',
      withCredentials: true,
      headers: headers
    })
  }

  resendEmailVerification(email: string) {
    return this.httpClient.post(this.API_URL + "api/auth/get-verification-token", {}, {
      observe: "response",
      params: {email: email}
    });
  }

  confirmEmail(email: string, code: string) {
    return this.httpClient.post(this.API_URL + "api/auth/verify-email", {}, {
      observe: "response",
      params: {email: email, code: code}
    });
  }

  sendPasswordResetLink(email: string) {
    return this.httpClient.post(this.API_URL + "api/auth/get-reset-password-link", {}, {
      observe: "response",
      params: {email: email}
    });
  }

  resetPassword(passwordResetInfo: { email: string, password: string, token: string }) {
    return this.httpClient.post(this.API_URL + "api/auth/reset-password", passwordResetInfo, {observe: "response"});
  }
}
