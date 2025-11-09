package com.felissedano.dailyreflect.journaling;

import java.time.LocalDate;
import java.util.ArrayList;

public record JournalDto(String content, ArrayList<String> tags, LocalDate date) {}
