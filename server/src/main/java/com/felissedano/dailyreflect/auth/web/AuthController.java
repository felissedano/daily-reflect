package com.felissedano.dailyreflect.auth.web;

import com.felissedano.dailyreflect.auth.service.dto.LoginDto;
import com.felissedano.dailyreflect.auth.service.dto.UserDto;
import com.felissedano.dailyreflect.auth.domain.model.User;
import com.felissedano.dailyreflect.auth.service.EmailVerificationService;
import com.felissedano.dailyreflect.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    final EmailVerificationService emailVerificationService;

    public AuthController(AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder,
                          UserService userService,
                          EmailVerificationService emailVerificationService
    ) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.emailVerificationService = emailVerificationService;
    }

    @GetMapping("/user")
    public String user() {
        return userService.findAll().toString();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDTO) {
        //TODO gotta test if this actually works in case of credentials are wrong and/or user does not exists
        try {
            System.out.println("AUTHENTICATING");
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException ex) {
            System.out.println("Bad Credentials");
            return new ResponseEntity<>("Email or password incorrect", HttpStatusCode.valueOf(401));
        } catch (DisabledException ex) {
            return new ResponseEntity<>("Account is disabled", HttpStatus.LOCKED);
        } catch (Exception ex) {
            System.out.println("Some other exceptions.");
            return new ResponseEntity<>("Something went wrong", HttpStatusCode.valueOf(500));
        }

        return ResponseEntity.ok("Login Successful");
    }

    @PostMapping("/register")
    public ResponseEntity<Optional<User>> register(@RequestBody UserDto userDto) {
        if (userService.checkIfUserNotExists(userDto)) {
            User user = userService.registerNormalUser(userDto);
            return new ResponseEntity<>(Optional.of(user), HttpStatusCode.valueOf(201));

        } else {
            return new ResponseEntity<>(Optional.empty(), HttpStatusCode.valueOf(404));
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String email, @RequestParam String code) {
        try {
            emailVerificationService.enableUser(email, code);
            return new ResponseEntity<>("Verification Success", HttpStatusCode.valueOf(201));

        } catch (IllegalStateException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Boolean> resendVerificationToken(@RequestParam String email) {
        //TODO implement it
        boolean isSend = emailVerificationService.resendVerificationEmail(email);
        return ResponseEntity.badRequest().build();
    }


}
