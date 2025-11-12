import { Routes } from '@angular/router';
import { JournalPageComponent } from './features/journal/page/journal-page/journal-page.component';
import { authGuard } from './core/auth/guard/auth.guard';
import { PageNotFoundComponent } from './static/page-not-found/page-not-found.component';

export const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () =>
      import('./core/auth/auth.routes').then((m) => m.authRoutes),
  },
  {
    path: 'journal',
    component: JournalPageComponent,
    canActivate: [authGuard],
  },
  { path: '', redirectTo: '/journal', pathMatch: 'full' },
  { path: '**', component: PageNotFoundComponent },
];
