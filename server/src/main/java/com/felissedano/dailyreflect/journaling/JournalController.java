package com.felissedano.dailyreflect.journaling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("api/journal")
public class JournalController {

    @Autowired
    JournalRepository journalRepository;

    @GetMapping("id/{id}")
    public ResponseEntity<Journal> getJournal(@PathVariable("id") long id) {
        Optional<Journal> journal = journalRepository.findById(id);
        return journal.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("date/{date}")
    public ResponseEntity<Journal> getJournalByDate(
            @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ) {
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
        Journal journal = new Journal(new Date(System.currentTimeMillis()), content, new ArrayList<>());
        Journal savedJournal = journalRepository.save(journal);
        return ResponseEntity.ok(savedJournal);

    }


}
