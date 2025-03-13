package com.felissedano.dailyreflect.auth.runner;

import com.felissedano.dailyreflect.auth.domain.model.Role;
import com.felissedano.dailyreflect.auth.domain.model.enums.RoleType;
import com.felissedano.dailyreflect.auth.domain.repository.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthDataLoader implements ApplicationRunner {

    private final RoleRepository roleRepository;

    public AuthDataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Application Starting and inserting initial role data");
        Optional<Role> roleUser = roleRepository.findByName(RoleType.ROLE_USER);
        if (roleUser.isEmpty()) {
            roleRepository.save(new Role(RoleType.ROLE_USER));
        }

        Optional<Role> roleAdmin = roleRepository.findByName(RoleType.ROLE_ADMIN);
        if (roleAdmin.isEmpty()) {
            roleRepository.save(new Role(RoleType.ROLE_ADMIN));
        }


    }
}
