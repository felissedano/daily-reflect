import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class CryptoService {
  private PBKDF2_ITERATIONS = 600000; //OWASP recommendation
  private SALT_LENGTH = 16;
  private IV_LENGTH = 12;

  private encoder = new TextEncoder();
  private decoder = new TextDecoder();

  private dataEncryptionKey: CryptoKey | null = null;

  async encryptData(data: string | string[]) {
    if (this.dataEncryptionKey == null) throw Error('DEK is not initialized');

    const iv = this.randomBytes(this.IV_LENGTH);

    const encodedData = this.encoder.encode(JSON.stringify(data));

    const params: AesGcmParams = { iv: iv, name: 'AES-GCM' };

    const encrypted = await crypto.subtle.encrypt(
      params,
      this.dataEncryptionKey,
      encodedData,
    );

    return {
      cipherText: this.toBase64(new Uint8Array(encrypted)),
      iv: this.toBase64(iv),
    };
  }

  async decryptData(
    cipherText: string,
    iv: string,
  ): Promise<string | string[]> {
    if (this.dataEncryptionKey == null)
      throw new Error('DEK is not initialized');

    const params: AesGcmParams = {
      name: 'AES-GCM',
      iv: new Uint8Array(this.fromBase64(iv)),
    };

    const decrypted = await crypto.subtle.decrypt(
      params,
      this.dataEncryptionKey,
      this.fromBase64(cipherText),
    );

    return JSON.parse(this.decoder.decode(decrypted));
  }

  // Should only be called during account creation
  async generateNewDekEncrypted(
    password: string,
  ): Promise<EncryptedDekProperties> {
    const dek = await crypto.subtle.generateKey(
      { name: 'AES-GCM', length: 256 },
      true,
      ['encrypt', 'decrypt'],
    );

    return this.encryptDek(password, dek);
  }

  // Should only be called during password changing process
  async rotateKekAndGetNewDekEncrypted(
    newPassword: string,
  ): Promise<EncryptedDekProperties> {
    if (this.dataEncryptionKey === null)
      throw new Error('Encryption key is not initialized');
    return this.encryptDek(newPassword, this.dataEncryptionKey);
  }

  async decryptAndSetDek(
    password: string,
    encryptedDek: string,
    salt: string,
    iv: string,
  ) {
    const kek = await this.deriveKek(
      password,
      new Uint8Array(this.fromBase64(salt)),
    );

    const decrypted = await crypto.subtle.decrypt(
      { name: 'AES-GCM', iv: new Uint8Array(this.fromBase64(iv)) },
      kek,
      this.fromBase64(encryptedDek),
    );

    this.dataEncryptionKey = await this.importDek(decrypted);

    return;
  }

  private async encryptDek(
    password: string,
    dek: CryptoKey,
  ): Promise<EncryptedDekProperties> {
    const salt = this.randomBytes(this.SALT_LENGTH);
    const iv = this.randomBytes(this.IV_LENGTH);

    const kek = await this.deriveKek(password, salt);
    const rawDek = await this.exportDek(dek);
    const params: AesGcmParams = { iv: iv, name: 'AES-GCM' };

    const encrypted = await crypto.subtle.encrypt(params, kek, rawDek);

    return {
      encryptedDek: this.toBase64(new Uint8Array(encrypted)),
      salt: this.toBase64(salt),
      iv: this.toBase64(iv),
    };
  }

  private async deriveKek(
    password: string,
    salt: Uint8Array<ArrayBuffer>,
  ): Promise<CryptoKey> {
    const baseKey = await crypto.subtle.importKey(
      'raw',
      this.encoder.encode(password),
      'PBKDF2',
      false,
      ['deriveKey'],
    );

    const pbkdf2Params: Pbkdf2Params = {
      name: 'PBKDF2',
      salt: salt,
      iterations: this.PBKDF2_ITERATIONS,
      hash: 'SHA-256',
    };

    const aesParams: AesDerivedKeyParams = { name: 'AES-GCM', length: 256 };

    return crypto.subtle.deriveKey(pbkdf2Params, baseKey, aesParams, false, [
      'encrypt',
      'decrypt',
    ]);
  }

  private async exportDek(dek: CryptoKey): Promise<ArrayBuffer> {
    return crypto.subtle.exportKey('raw', dek);
  }

  private async importDek(raw: ArrayBuffer): Promise<CryptoKey> {
    return crypto.subtle.importKey('raw', raw, 'AES-GCM', true, [
      'encrypt',
      'decrypt',
    ]);
  }

  private randomBytes(length: number): Uint8Array<ArrayBuffer> {
    return crypto.getRandomValues(new Uint8Array(length));
  }

  private toBase64(buffer: Uint8Array<ArrayBuffer>): string {
    return btoa(String.fromCharCode(...new Uint8Array(buffer)));
  }

  private fromBase64(b64: string): ArrayBuffer {
    return Uint8Array.from(atob(b64), (c) => c.charCodeAt(0)).buffer;
  }
}

interface EncryptedDekProperties {
  encryptedDek: string;
  salt: string;
  iv: string;
}
