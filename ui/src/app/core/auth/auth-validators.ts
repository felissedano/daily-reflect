import { AbstractControl, ValidationErrors, Validators } from '@angular/forms';

export const samePasswordValidator = (
  control: AbstractControl,
): ValidationErrors | null => {
  const password: string | undefined = control.parent?.get('password')?.value;
  const confirmPassword: string | undefined =
    control.parent?.get('confirmPassword')?.value;
  if (password === confirmPassword) {
    return null;
  } else {
    return { samePassword: true };
  }
};

export const passwordValidatorArray = [
  Validators.minLength(8),
  Validators.pattern('((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,30})'),
];
