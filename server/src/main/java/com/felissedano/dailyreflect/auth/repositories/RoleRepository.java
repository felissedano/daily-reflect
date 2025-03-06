package com.felissedano.dailyreflect.auth.repositories;


import com.felissedano.dailyreflect.auth.models.Role;
import com.felissedano.dailyreflect.auth.models.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Optional<Role> findByName(RoleType roleName);
}
