package com.felissedano.dailyreflect.journaling;

import java.time.LocalDate;
import java.util.List;

public record JournalDto(String content, List<String> tags, LocalDate date) {}
