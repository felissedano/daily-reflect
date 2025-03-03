import {Component} from '@angular/core';
import {AuthenticationService} from "../../authentication.service";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {AuthLayoutComponent} from "../../../layouts/auth-layout/auth-layout.component";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatFormField, MatInput, MatSuffix} from "@angular/material/input";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatDateRangeInput} from "@angular/material/datepicker";
import {MatIcon} from "@angular/material/icon";
import {RouterLink} from "@angular/router";
import {MatFormFieldModule} from "@angular/material/form-field";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-login-page',
  imports: [MatSlideToggleModule, AuthLayoutComponent, MatButton, MatInput, ReactiveFormsModule, MatFormFieldModule, MatIcon, MatIconButton, MatSuffix, RouterLink, NgIf],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss'
})
export class LoginPageComponent {


  loginForm = new FormGroup({
    email: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  })

  loginFailed = false;

  onSubmit(){
    console.log(this.loginForm.controls)
    console.log(this.loginForm.value)
    if (this.loginForm.invalid) {
      return
    }

    //TODO
  }

  hidePwd = true;
  toggleHidePwd() {
    this.hidePwd = !this.hidePwd;
  }
  getInputType() {
    return this.hidePwd ? 'password' : 'text';
  }
  getVisibilityIcon() {
    return this.hidePwd ? 'visibility' : 'visibility_off';
  }
}
