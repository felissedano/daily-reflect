package com.felissedano.dailyreflect.auth.domain.repository;

import com.felissedano.dailyreflect.auth.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

    public Boolean existsByEmail(String email);

    public Boolean existsByUsername(String email);
}
