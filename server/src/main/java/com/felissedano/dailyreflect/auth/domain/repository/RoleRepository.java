package com.felissedano.dailyreflect.auth.domain.repository;

import com.felissedano.dailyreflect.auth.domain.model.Role;
import com.felissedano.dailyreflect.auth.domain.model.enums.RoleType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Optional<Role> findByName(RoleType roleName);
}
