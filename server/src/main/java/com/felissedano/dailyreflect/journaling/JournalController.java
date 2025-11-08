package com.felissedano.dailyreflect.journaling;

import com.felissedano.dailyreflect.profile.Profile;
import com.felissedano.dailyreflect.profile.ProfileRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/journal")
public class JournalController {

    public static final Logger log = LoggerFactory.getLogger(JournalController.class);

    final JournalRepository journalRepository;

    final ProfileRepository profileRepository;

    final JournalService journalService;

    public JournalController(JournalRepository journalRepository, ProfileRepository profileRepository, JournalService journalService) {
        this.journalRepository = journalRepository;
        this.profileRepository = profileRepository;
		this.journalService = journalService;
    }

    // @GetMapping("id/{id}")
    // public ResponseEntity<Journal> getJournal(@PathVariable("id") long id) {
    //     Optional<Journal> journal = journalRepository.findById(id);
    //     return journal.map(ResponseEntity::ok)
    //             .orElseGet(() -> ResponseEntity.notFound().build());
    // }

    @GetMapping("date/{date}")
    public ResponseEntity<JournalDto> getJournalByDate(
            @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

        UserDetails principal = (UserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JournalDto journalDto = journalService.getJournalDto(date, principal.getUsername());
        if (journalDto == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(journalDto);
        }
    }

    @PostMapping("edit")
    public ResponseEntity<Void> editJournal(@RequestBody JournalDto journalDto) {
        UserDetails principal = (UserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        journalService.createOrUpdateJournal(journalDto, principal.getUsername());;
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("{id}")
    public void deleteJournal(@PathVariable("id") long id) {
        journalRepository.deleteById(id);
    }

    @GetMapping("mock/{content}")
    public ResponseEntity<Journal> addMockJournal(@PathVariable String content) {
        UserDetails principal = (UserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Profile profile =
                profileRepository.findByUserEmail(principal.getUsername()).orElseThrow();

        ArrayList<String> tagList = new ArrayList<>();
        tagList.addAll(Arrays.asList(new String[] {"feeling good", "cat", "!@#$"}));
        Journal journal = new Journal(new Date(System.currentTimeMillis()), content, tagList, profile);
        // new ArrayList<>(), null);
        Journal savedJournal = journalRepository.save(journal);
        return ResponseEntity.ok(savedJournal);
    }
}
