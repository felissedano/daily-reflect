package com.felissedano.dailyreflect.auth.service.dto;

public record EncryptionPropertiesDTO(String encryptedDek, String salt, String iv) {}
