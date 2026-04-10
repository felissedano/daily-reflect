import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { TranslateModule } from '@ngx-translate/core';
import { RouterModule } from '@angular/router';

import { ForgotPasswordPageComponent } from './forgot-password-page.component';

describe('ForgotPasswordPageComponent', () => {
  let component: ForgotPasswordPageComponent;
  let fixture: ComponentFixture<ForgotPasswordPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ForgotPasswordPageComponent, TranslateModule.forRoot(), RouterModule.forRoot([])],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ForgotPasswordPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
