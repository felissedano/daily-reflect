import { Component } from '@angular/core';
import { PrimaryButtonComponent } from '../../../../shared/components/primary-button/primary-button.component';

@Component({
  selector: 'app-journal-page',
  standalone: true,
  imports: [PrimaryButtonComponent],
  templateUrl: './journal-page.component.html',
  styleUrl: './journal-page.component.scss',
})
export class JournalPageComponent {}
