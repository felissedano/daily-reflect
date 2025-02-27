import { Component } from '@angular/core';

@Component({
  selector: 'app-primary-button',
  standalone: true,
  templateUrl: './primary-button.component.html',
  styleUrl: './primary-button.component.scss',
})
export class PrimaryButtonComponent {

  constructor() {
  }

  ngInit() {

  }

  log(){
    console.log("Click")
  }


}
