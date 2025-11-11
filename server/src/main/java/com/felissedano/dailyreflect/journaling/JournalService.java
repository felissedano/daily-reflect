package com.felissedano.dailyreflect.journaling;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface JournalService {

    public void createOrUpdateJournal(JournalDto journalDto, String userEmail);

    public JournalDto getJournalDto(LocalDate date, String userEmail);

    public void deleteJournal(LocalDate date, String userEmail);

    public List<JournalDto> getJournalsByYearMonth(YearMonth yearMonth, String userEmail);
}
