package com.felissedano.dailyreflect.journaling;

import com.felissedano.dailyreflect.profile.Profile;
import com.felissedano.dailyreflect.profile.ProfileNotFoundException;
import com.felissedano.dailyreflect.profile.ProfileRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * DefaultJournalService
 */
@Service
public class DefaultJournalService implements JournalService {

    private final ProfileRepository profileRepository;
    private final JournalRepository journalRepository;

    public DefaultJournalService(JournalRepository journalRepository, ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
        this.journalRepository = journalRepository;
    }

    @Override
    public void createOrUpdateJournal(JournalDto journalDto, String userEmail) {
        Profile profile = profileRepository
                .findByUserEmail(userEmail)
                .orElseThrow(
                        () -> new ProfileNotFoundException(
                                "Profile associated with the user does not exist. Likely something went wrong in the user creation process"));

        Optional<Journal> journalOpt = journalRepository.findByDateAndProfile(journalDto.date(), profile);
        if (journalOpt.isEmpty()) {
            Journal journal = new Journal(journalDto.date(), journalDto.content(), journalDto.tags(), profile);
            journalRepository.save(journal);
        } else {
            Journal curJournal = journalOpt.get();
            curJournal.setContent(journalDto.content());
            curJournal.setTags(journalDto.tags());
            journalRepository.save(curJournal);
        }
    }
}
