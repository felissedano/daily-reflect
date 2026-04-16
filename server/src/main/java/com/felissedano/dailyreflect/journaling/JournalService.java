package com.felissedano.dailyreflect.journaling;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface JournalService {

    void createOrUpdateJournal(JournalDto journalDto, String userEmail);

    Optional<JournalDto> getJournalDto(LocalDate date, String userEmail);

    void deleteJournal(LocalDate date, String userEmail);

    List<JournalDto> getJournalsByYearMonth(YearMonth yearMonth, String userEmail);
}
