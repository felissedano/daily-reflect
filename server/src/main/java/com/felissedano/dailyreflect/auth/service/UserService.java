package com.felissedano.dailyreflect.auth.service;

import com.felissedano.dailyreflect.auth.domain.model.User;
import com.felissedano.dailyreflect.auth.service.dto.UserDto;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    User save(User user);

    Optional<User> findUserByEmail(String email);

    Boolean checkIfUserNotExists(UserDto userDto);

    @Transactional
    User registerNormalUser(UserDto userDto);

    @Transactional
    boolean deleteUser(String email);
}
