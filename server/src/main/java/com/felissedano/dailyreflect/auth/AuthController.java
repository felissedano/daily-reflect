package com.felissedano.dailyreflect.auth;

import com.felissedano.dailyreflect.auth.dtos.LoginDTO;
import com.felissedano.dailyreflect.auth.dtos.UserDto;
import com.felissedano.dailyreflect.auth.models.User;
import com.felissedano.dailyreflect.auth.services.UserService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/auth")
public class AuthController {

    final AuthenticationManager authenticationManager;

    final PasswordEncoder passwordEncoder;

    final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @GetMapping("/user")
    public String user() {
        return userService.findAll().toString();
    }
//    @PostMapping("/login")
//    ResponseEntity<String> authenticateUser(@PathVariable String email) {
//
//    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody LoginDTO loginDTO) {

        return new ResponseEntity<>(false, HttpStatusCode.valueOf(403));
    }

    @PostMapping("/register")
    public ResponseEntity<Optional<User>> register(@RequestBody UserDto userDto) {
        if (userService.checkIfUserNotExists(userDto)) {
            String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
            User user = userService.registerNormalUser(userDto, encryptedPassword);
            return new ResponseEntity<>(Optional.of(user), HttpStatusCode.valueOf(201));

        } else {
            return new ResponseEntity<>(Optional.empty(), HttpStatusCode.valueOf(404));
        }
    }

}
