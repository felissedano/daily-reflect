import {Component} from '@angular/core';
import {AuthenticationService} from "../../authentication.service";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {AuthLayoutComponent} from "../../../layouts/auth-layout/auth-layout.component";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatFormField, MatInput, MatSuffix} from "@angular/material/input";
import {FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatDateRangeInput} from "@angular/material/datepicker";
import {MatIcon} from "@angular/material/icon";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-login-page',
  imports: [MatSlideToggleModule, AuthLayoutComponent, MatButton, MatInput, ReactiveFormsModule, MatFormField, MatDateRangeInput, MatIcon, MatIconButton, MatSuffix, RouterLink],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss'
})
export class LoginPageComponent {


  hidePwd = true;
  toggleHidePwd() {
    this.hidePwd = !this.hidePwd;
  }
  getInputType() {
    return this.hidePwd ? 'password' : 'text';
  }
  getVisibilityIcon() {
    return this.hidePwd ? 'visibility_off' : 'visibility';
  }
}
