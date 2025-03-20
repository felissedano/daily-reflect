import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {map, tap} from "rxjs";

//timer that after coundown checks if user made any interaction, if yes, checkauth, if logged in, reset user interaction to false
// event listern that on user make action, set user made action to true
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
    return this.httpClient.delete<string>(this.API_URL + "api/auth/logout", {withCredentials: true, observe: 'response'}).pipe(
        map(res => res.ok)
      );
  }

  checkAuthStatus() {
    return this.httpClient.get<boolean>(this.API_URL + "api/auth/is-auth", {
      withCredentials: true,
      observe: 'response'
    }).pipe(map((res) => res.body === null ? false : res.body === true ))
  }
}
