import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GetVerifyEmailPageComponent } from './get-verify-email-page.component';

describe('VerifyEmailPageComponent', () => {
  let component: GetVerifyEmailPageComponent;
  let fixture: ComponentFixture<GetVerifyEmailPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GetVerifyEmailPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GetVerifyEmailPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
