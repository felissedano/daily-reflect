package com.felissedano.dailyreflect.auth.services;

import com.felissedano.dailyreflect.auth.RoleRepository;
import com.felissedano.dailyreflect.auth.dtos.UserDto;
import com.felissedano.dailyreflect.auth.UserRepository;
import com.felissedano.dailyreflect.auth.models.Role;
import com.felissedano.dailyreflect.auth.models.User;
import com.felissedano.dailyreflect.auth.models.enums.RoleType;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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

        return userRepository.save(new User(userDto.username(),userDto.email(),encryptedPassword, roles));
    }
}