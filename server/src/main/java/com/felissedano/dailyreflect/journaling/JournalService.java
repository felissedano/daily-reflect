package com.felissedano.dailyreflect.journaling;

import java.util.Date;

public interface JournalService {

    public void createOrUpdateJournal(JournalDto journalDto, String userEmail);

    public JournalDto getJournalDto(Date date, String userEmail);
}
