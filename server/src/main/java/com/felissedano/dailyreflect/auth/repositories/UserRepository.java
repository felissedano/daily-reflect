package com.felissedano.dailyreflect.auth.repositories;

import com.felissedano.dailyreflect.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

    public Boolean existsByEmail(String email);

    public Boolean existsByUsername(String email);

}
