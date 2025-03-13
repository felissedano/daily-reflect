package com.felissedano.dailyreflect.auth.domain.repository;


import com.felissedano.dailyreflect.auth.domain.model.Role;
import com.felissedano.dailyreflect.auth.domain.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Optional<Role> findByName(RoleType roleName);
}
