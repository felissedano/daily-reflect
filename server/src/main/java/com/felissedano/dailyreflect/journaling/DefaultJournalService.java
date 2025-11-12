package com.felissedano.dailyreflect.journaling;

import com.felissedano.dailyreflect.profile.Profile;
import com.felissedano.dailyreflect.profile.ProfileNotFoundException;
import com.felissedano.dailyreflect.profile.ProfileRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultJournalService implements JournalService {

    private static final Logger log = LoggerFactory.getLogger(DefaultJournalService.class);

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

    @Override
    public Optional<JournalDto> getJournalDto(LocalDate date, String userEmail) {
        Profile profile = profileRepository
                .findByUserEmail(userEmail)
                .orElseThrow(
                        () -> new ProfileNotFoundException(
                                "Profile associated with the user does not exist. Likely something went wrong in the user creation process"));

        Optional<Journal> journalOpt = journalRepository.findByDateAndProfile(date, profile);

        return journalOpt.map(journal -> new JournalDto(journal.getContent(), journal.getTags(), journal.getDate()));
    }

    @Override
    public void deleteJournal(LocalDate date, String userEmail) {
        Profile profile = profileRepository
                .findByUserEmail(userEmail)
                .orElseThrow(
                        () -> new ProfileNotFoundException(
                                "Profile associated with the user does not exist. Likely something went wrong in the user creation process"));

        Optional<Journal> journalOpt = journalRepository.findByDateAndProfile(date, profile);
        if (journalOpt.isPresent()) {
            journalRepository.deleteById(journalOpt.get().getId());
        }
    }

    @Override
    public List<JournalDto> getJournalsByYearMonth(YearMonth yearMonth, String userEmail) {
        Profile profile = profileRepository
                .findByUserEmail(userEmail)
                .orElseThrow(
                        () -> new ProfileNotFoundException(
                                "Profile associated with the user does not exist. Likely something went wrong in the user creation process"));

        List<Journal> journals =
                journalRepository.findByProfileAndDateBetween(profile, yearMonth.atDay(1), yearMonth.atEndOfMonth());

        List<JournalDto> journalDtos = journals.stream()
                .map((journal) -> new JournalDto(journal.getContent(), journal.getTags(), journal.getDate()))
                .collect(Collectors.toList());

        return journalDtos;
    }
}
