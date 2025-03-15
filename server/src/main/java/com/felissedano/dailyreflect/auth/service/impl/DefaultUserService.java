package com.felissedano.dailyreflect.auth.service.impl;

import com.felissedano.dailyreflect.auth.AuthUtils;
import com.felissedano.dailyreflect.auth.domain.repository.RoleRepository;
import com.felissedano.dailyreflect.auth.service.EmailVerificationService;
import com.felissedano.dailyreflect.auth.service.UserService;
import com.felissedano.dailyreflect.auth.service.dto.UserDto;
import com.felissedano.dailyreflect.auth.domain.repository.UserRepository;
import com.felissedano.dailyreflect.auth.domain.model.Role;
import com.felissedano.dailyreflect.auth.domain.model.User;
import com.felissedano.dailyreflect.auth.domain.model.enums.RoleType;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    public DefaultUserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, EmailVerificationService emailVerificationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailVerificationService = emailVerificationService;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return  userRepository.findByEmail(email);
    }

    @Override
    public Boolean checkIfUserNotExists(UserDto userDto) {
        return !userRepository.existsByEmail(userDto.email()) && !userRepository.existsByUsername(userDto.email());
    }

    @Transactional
    @Override
    public User registerNormalUser(UserDto userDto) {
        String encryptedPassword = passwordEncoder.encode(userDto.password());
        Set<Role> roles = new HashSet<>(1);
        Role roleUser = roleRepository.findByName(RoleType.ROLE_USER).orElseThrow();
        System.out.println(roleUser.getName());
        roles.add(roleUser);

        User newUser = new User(userDto.username(),userDto.email(),encryptedPassword, roles);

        String verificationToken = AuthUtils.generateVerificationToken();
        newUser.setVerificationCode(verificationToken);
        newUser.setCodeExpiration(AuthUtils.generateTokenExpirationDate());


        //TODO MailSender send email
        emailVerificationService.sendVerificationEmail(newUser.getEmail(), newUser.getUsername(), verificationToken);

        return userRepository.save(newUser);
    }


    @Transactional
    @Override
    public boolean deleteUser(String email) {
        Optional<User> userToDelete = userRepository.findByEmail(email);

        if (userToDelete.isEmpty()) {
            return  false;
        }
        userRepository.delete(userToDelete.get());
        return true;
    }
}