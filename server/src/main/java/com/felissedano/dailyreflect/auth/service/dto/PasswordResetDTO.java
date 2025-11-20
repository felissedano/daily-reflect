package com.felissedano.dailyreflect.auth.service.dto;

public record PasswordResetDTO(String email, String password, String token) {}
