package com.felissedano.dailyreflect.journaling;

import com.felissedano.dailyreflect.profile.Profile;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository extends JpaRepository<Journal, Long> {
    List<Journal> findAllByOrderByDateDesc();

    Optional<Journal> findByDate(LocalDate date);

    Optional<Journal> findByDateAndProfile(LocalDate date, Profile profile);

    List<Journal> findByProfileAndDateBetween(Profile profile, LocalDate start, LocalDate end);
}
