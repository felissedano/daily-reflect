import { Component } from '@angular/core';
import { ComponentsModule } from '../../../../shared/components/components.module';
@Component({
  selector: 'app-journal-page',
  standalone: true,
  imports: [ComponentsModule],
  templateUrl: './journal-page.component.html',
  styleUrl: './journal-page.component.scss',
})
export class JournalPageComponent {}
