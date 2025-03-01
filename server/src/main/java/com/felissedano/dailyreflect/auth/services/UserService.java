package com.felissedano.dailyreflect.auth.services;

import com.felissedano.dailyreflect.auth.dtos.UserDto;
import com.felissedano.dailyreflect.auth.UserRepository;
import com.felissedano.dailyreflect.auth.models.Role;
import com.felissedano.dailyreflect.auth.models.User;
import com.felissedano.dailyreflect.auth.models.enums.RoleType;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return !userRepository.existsByEmail(userDto.getEmail()) && !userRepository.existsByUsername(userDto.getEmail());
    }

    public User registerNormalUser(UserDto userDto, String encryptedPassword) {
        Set<Role> roles = new HashSet<>(1);
        roles.add(new Role(RoleType.ROLE_USER));
        return userRepository.save(new User(userDto.getUsername(),userDto.getEmail(),encryptedPassword, roles));
    }
}