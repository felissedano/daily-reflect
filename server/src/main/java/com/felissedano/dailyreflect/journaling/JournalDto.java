package com.felissedano.dailyreflect.journaling;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record JournalDto(
        String content,
        List<String> tags,
        @NotNull
        LocalDate date,
        String iv) {}
