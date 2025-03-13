package com.felissedano.dailyreflect.auth.domain.repository;

import com.felissedano.dailyreflect.auth.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

    public Boolean existsByEmail(String email);

    public Boolean existsByUsername(String email);

}
