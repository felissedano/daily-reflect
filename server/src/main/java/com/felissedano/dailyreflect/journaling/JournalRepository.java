package com.felissedano.dailyreflect.journaling;

import com.felissedano.dailyreflect.profile.Profile;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository extends JpaRepository<Journal, Long> {
    List<Journal> findAllByOrderByDateDesc();

    Optional<Journal> findByDate(Date date);

    Optional<Journal> findByDateAndProfile(Date date, Profile profile);
}
