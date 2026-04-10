import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { TranslateModule } from '@ngx-translate/core';
import { RouterModule } from '@angular/router';

import { GetVerifyEmailPageComponent } from './get-verify-email-page.component';

describe('VerifyEmailPageComponent', () => {
  let component: GetVerifyEmailPageComponent;
  let fixture: ComponentFixture<GetVerifyEmailPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GetVerifyEmailPageComponent, TranslateModule.forRoot(), RouterModule.forRoot([])],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(GetVerifyEmailPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
