package com.felissedano.dailyreflect.journaling;

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

import com.felissedano.dailyreflect.profile.Profile;
import com.felissedano.dailyreflect.profile.ProfileRepository;

@RestController
@RequestMapping("api/journal")
public class JournalController {

    public static final Logger log = LoggerFactory.getLogger(JournalController.class);

    final JournalRepository journalRepository;

    final ProfileRepository profileRepository;

    public JournalController(JournalRepository journalRepository, ProfileRepository profileRepository) {
        this.journalRepository = journalRepository;
        this.profileRepository = profileRepository;
    }

    @GetMapping("id/{id}")
    public ResponseEntity<Journal> getJournal(@PathVariable("id") long id) {
        Optional<Journal> journal = journalRepository.findById(id);
        return journal.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("date/{date}")
    public ResponseEntity<Journal> getJournalByDate(
            @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        Optional<Journal> journal = journalRepository.findByDate(date);
        return journal.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("add")
    public Journal addJournal(@RequestBody Journal journal) {
        return journalRepository.save(journal);
    }

    @DeleteMapping("{id}")
    public void deleteJournal(@PathVariable("id") long id) {
        journalRepository.deleteById(id);
    }

    @GetMapping("mock/{content}")
    public ResponseEntity<Journal> addMockJournal(@PathVariable String content) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Profile profile = profileRepository.findByUserEmail(principal.getUsername()).orElseThrow();

        ArrayList<String> tagList = new ArrayList<>();
        tagList.addAll(Arrays.asList(new String[] { "feeling good", "cat", "!@#$" }));
        Journal journal = new Journal(new Date(System.currentTimeMillis()), content, tagList, profile);
        // new ArrayList<>(), null);
        Journal savedJournal = journalRepository.save(journal);
        return ResponseEntity.ok(savedJournal);

    }

}
