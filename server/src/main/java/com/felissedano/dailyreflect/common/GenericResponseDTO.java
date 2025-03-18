package com.felissedano.dailyreflect.common;

public record GenericResponseDTO(int statusCode, boolean isSuccess, String message) {
}
