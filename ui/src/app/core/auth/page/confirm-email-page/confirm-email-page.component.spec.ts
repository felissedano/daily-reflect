import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { TranslateModule } from '@ngx-translate/core';
import { RouterModule } from '@angular/router';

import { ConfirmEmailPageComponent } from './confirm-email-page.component';

describe('ConfirmEmailPageComponent', () => {
  let component: ConfirmEmailPageComponent;
  let fixture: ComponentFixture<ConfirmEmailPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmEmailPageComponent, TranslateModule.forRoot(), RouterModule.forRoot([])],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ConfirmEmailPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
