package com.felissedano.dailyreflect.journaling;

import java.time.LocalDate;

public interface JournalService {

    public void createOrUpdateJournal(JournalDto journalDto, String userEmail);

    public JournalDto getJournalDto(LocalDate date, String userEmail);

    public void deleteJournal(LocalDate date, String userEmail);
}
