package com.felissedano.dailyreflect.auth.web;

import com.felissedano.dailyreflect.auth.domain.model.User;
import com.felissedano.dailyreflect.auth.service.UserService;
import com.felissedano.dailyreflect.auth.service.dto.EncryptionPropertiesDTO;
import com.felissedano.dailyreflect.common.GenericResponseDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("dek")
    @PreAuthorize("hasRole('ROLE_USER')")
    public EncryptionPropertiesDTO getEncryptionProperties(Authentication auth) {
        Optional<User> userOpt = userService.findUserByEmail(auth.getName());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Could not find user with email: " + auth.getName());
        }

        return new EncryptionPropertiesDTO(userOpt.get().getEncryptedDek(), userOpt.get().getSalt(), userOpt.get().getIv());
    }

    @GetMapping("hello")
    public GenericResponseDTO hello() {
        return new GenericResponseDTO(200, true, "Hello World!");
    }

    @GetMapping("user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public GenericResponseDTO user(Authentication auth) {
        String message = "Hello " + auth.getName() + " with authorities: " + auth.getAuthorities();
        return new GenericResponseDTO(200, true, message);
    }

    @GetMapping("admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public GenericResponseDTO admin(Authentication auth) {
        String message = "Hello admin " + auth.getName() + " with authorities: " + auth.getAuthorities();
        return new GenericResponseDTO(200, true, message);
    }

    @PostMapping("test-post")
    public GenericResponseDTO testPostRequest() {
        return new GenericResponseDTO(200, true, "CORS and CSRF working correctly");
    }
}
