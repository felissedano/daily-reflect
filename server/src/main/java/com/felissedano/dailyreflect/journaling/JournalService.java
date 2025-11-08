package com.felissedano.dailyreflect.journaling;

public interface JournalService {

    public void createOrUpdateJournal(JournalDto journalDto, String userEmail);
}
