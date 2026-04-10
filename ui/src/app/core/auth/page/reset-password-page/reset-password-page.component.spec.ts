import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { TranslateModule } from '@ngx-translate/core';
import { RouterModule } from '@angular/router';

import { ResetPasswordPageComponent } from './reset-password-page.component';

describe('ResetPasswordPageComponent', () => {
  let component: ResetPasswordPageComponent;
  let fixture: ComponentFixture<ResetPasswordPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResetPasswordPageComponent, TranslateModule.forRoot(), RouterModule.forRoot([])],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ResetPasswordPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
