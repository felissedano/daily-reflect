package com.felissedano.dailyreflect.auth.services;

import com.felissedano.dailyreflect.auth.repositories.RoleRepository;
import com.felissedano.dailyreflect.auth.dtos.UserDto;
import com.felissedano.dailyreflect.auth.repositories.UserRepository;
import com.felissedano.dailyreflect.auth.models.Role;
import com.felissedano.dailyreflect.auth.models.User;
import com.felissedano.dailyreflect.auth.models.enums.RoleType;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, EmailVerificationService emailVerificationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailVerificationService = emailVerificationService;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUserByEmail(String email) {
        return  userRepository.findByEmail(email);
    }

    public Boolean checkIfUserNotExists(UserDto userDto) {
        return !userRepository.existsByEmail(userDto.email()) && !userRepository.existsByUsername(userDto.email());
    }

    @Transactional
    public User registerNormalUser(UserDto userDto) {
        String encryptedPassword = passwordEncoder.encode(userDto.password());
        Set<Role> roles = new HashSet<>(1);
        Role roleUser = roleRepository.findByName(RoleType.ROLE_USER).orElseThrow();
        System.out.println(roleUser.getName());
        roles.add(roleUser);

        User newUser = new User(userDto.username(),userDto.email(),encryptedPassword, roles);

        String verificationCode = UUID.randomUUID().toString();
        newUser.setVerificationCode(verificationCode);
        newUser.setCodeExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 8));


        //TODO MailSender send email
        emailVerificationService.sendVerificationEmail(newUser.getEmail(), newUser.getUsername(), verificationCode);

        return userRepository.save(newUser);
    }


    @Transactional
    public boolean deleteUser(String email) {
        Optional<User> userToDelete = userRepository.findByEmail(email);

        if (userToDelete.isEmpty()) {
            return  false;
        }
        userRepository.delete(userToDelete.get());
        return true;
    }
}