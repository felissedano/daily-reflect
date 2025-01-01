package com.felissedano.dailyreflect.repositories;

import com.felissedano.dailyreflect.models.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface JournalRepository extends JpaRepository<Journal, Long> {
    List<Journal> findAllByOrderByDateDesc();

    Optional<Journal> findByDate(Date date);

}
