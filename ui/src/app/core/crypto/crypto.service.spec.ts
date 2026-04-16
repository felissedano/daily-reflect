import { TestBed } from '@angular/core/testing';
import { CryptoService } from './crypto.service';
import is = jasmine.is;

describe('CryptoService', () => {
  let service: CryptoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CryptoService);
  });

  const password = 'StrongPassword123!';
  const wrongPassword = 'WrongPassword!';
  const sampleData = 'A new journal';

  it('should encrypt and decrypt DEK correctly', async () => {
    const dekProperties = await service.generateNewDekEncrypted(password);

    expect(dekProperties.encryptedDek).toBeTruthy();
    expect(dekProperties.salt).toBeTruthy();
    expect(dekProperties.iv).toBeTruthy();
  });

  it('should fail to decrypt DEK with wrong password', async () => {
    const dekProperties = await service.generateNewDekEncrypted(password);

    let isRightPasswordSucceed = false;
    try {
      await service.decryptAndSetDek(
        password,
        dekProperties.encryptedDek,
        dekProperties.salt,
        dekProperties.iv,
      );
      isRightPasswordSucceed = true;
    } catch (_) {
      isRightPasswordSucceed = false;
    }
    expect(isRightPasswordSucceed).toBeTrue();

    let isWrongPasswordFail = false;

    try {
      await service.decryptAndSetDek(
        wrongPassword,
        dekProperties.encryptedDek,
        dekProperties.salt,
        dekProperties.iv,
      );
    } catch (_) {
      isWrongPasswordFail = true;
    }

    expect(isWrongPasswordFail).toBeTrue();
  });

  it('should encrypt and decrypt data correctly', async () => {
    const dekProperties = await service.generateNewDekEncrypted(password);
    await service.decryptAndSetDek(
      password,
      dekProperties.encryptedDek,
      dekProperties.salt,
      dekProperties.iv,
    );

    const encrypted = await service.encryptData(sampleData);

    expect(encrypted.cipherText).toBeTruthy();
    expect(encrypted.iv).toBeTruthy();

    const decrypted = await service.decryptData(
      encrypted.cipherText,
      encrypted.iv,
    );

    expect(decrypted).toEqual(sampleData);
  });

  it('should produce different ciphertexts for same input (random IV)', async () => {
    const dekProperties = await service.generateNewDekEncrypted(password);
    await service.decryptAndSetDek(
      password,
      dekProperties.encryptedDek,
      dekProperties.salt,
      dekProperties.iv,
    );

    const encrypted1 = await service.encryptData(sampleData);
    const encrypted2 = await service.encryptData(sampleData);

    expect(encrypted1.cipherText).not.toEqual(encrypted2.cipherText);
    expect(encrypted1.iv).not.toEqual(encrypted2.iv);
  });

  it('should fail to decrypt with wrong DEK', async () => {
    const dek1 = await service.generateNewDekEncrypted(password)
    const dek2 = await service.generateNewDekEncrypted(password);

    await service.decryptAndSetDek(password, dek1.encryptedDek, dek1.salt, dek1.iv);

    const encrypted = await service.encryptData(sampleData);

    let failed = false;

    try {
      await service.decryptAndSetDek(password, dek2.encryptedDek, dek2.salt, dek2.iv);
      await service.decryptData(encrypted.cipherText, encrypted.iv);
    } catch (_) {
      failed = true;
    }

    expect(failed).toBeTrue();
  });

  it('should preserve data integrity (tampering detection)', async () => {
    const dekProperties = await service.generateNewDekEncrypted(password);
    await service.decryptAndSetDek(
      password,
      dekProperties.encryptedDek,
      dekProperties.salt,
      dekProperties.iv,
    );
    const encrypted = await service.encryptData(sampleData);

    // Tamper with ciphertext
    const tampered = encrypted.cipherText.slice(0, -2) + 'ab';

    let failed = false;

    try {
      await service.decryptData(tampered, encrypted.iv);
    } catch (_) {
      failed = true;
    }

    expect(failed).toBeTrue();
  });


  it('should rotate kek and still be able to decrypt old data', async () => {
    const dekProperties = await service.generateNewDekEncrypted(password);
    await service.decryptAndSetDek(
      password,
      dekProperties.encryptedDek,
      dekProperties.salt,
      dekProperties.iv,
    );
    const encrypted = await service.encryptData(sampleData);

    const newPassword = "NewPassword123!";

    const newDekProperties = await service.rotateKekAndGetNewDekEncrypted(newPassword);
    await service.decryptAndSetDek(newPassword, newDekProperties.encryptedDek, newDekProperties.salt, newDekProperties.iv);

    let failed = false;
    try {
      const decrypted = await service.decryptData(
        encrypted.cipherText,
        encrypted.iv,
      );

      expect(decrypted).toEqual(sampleData);

    } catch (_) {
      failed = true;
    }

    expect(failed).toBeFalse();
  })
});
