package com.felissedano.dailyreflect.journaling;

import java.sql.Date;
import java.util.ArrayList;

public record JournalDto(String content, ArrayList<String> tags, Date date) {}
