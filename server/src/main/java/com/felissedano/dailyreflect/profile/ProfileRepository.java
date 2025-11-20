package com.felissedano.dailyreflect.profile;

import com.felissedano.dailyreflect.auth.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByUser(User user);

    Optional<Profile> findByUserEmail(String email);
}
