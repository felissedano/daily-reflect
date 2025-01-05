import { Routes } from '@angular/router';
import { JournalPageComponent } from './features/journal/pages/journal-page/journal-page.component';

export const routes: Routes = [
  { path: '', component: JournalPageComponent },
  { path: 'journal', component: JournalPageComponent },
];
