package com.felissedano.dailyreflect.journaling;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface JournalService {

    public void createOrUpdateJournal(JournalDto journalDto, String userEmail);

    public Optional<JournalDto> getJournalDto(LocalDate date, String userEmail);

    public void deleteJournal(LocalDate date, String userEmail);

    public List<JournalDto> getJournalsByYearMonth(YearMonth yearMonth, String userEmail);
}
