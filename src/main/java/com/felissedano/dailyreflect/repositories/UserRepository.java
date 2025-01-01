package com.felissedano.dailyreflect.repositories;

import com.felissedano.dailyreflect.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
