package com.felissedano.dailyreflect.auth.web;

import com.felissedano.dailyreflect.auth.service.PasswordService;
import com.felissedano.dailyreflect.auth.service.dto.LoginDto;
import com.felissedano.dailyreflect.auth.service.dto.PasswordResetDTO;
import com.felissedano.dailyreflect.auth.service.dto.UserDto;
import com.felissedano.dailyreflect.auth.domain.model.User;
import com.felissedano.dailyreflect.auth.service.EmailVerificationService;
import com.felissedano.dailyreflect.auth.service.UserService;
import com.felissedano.dailyreflect.common.GenericResponseDTO;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
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

    final UserService userService;

    final EmailVerificationService emailVerificationService;

    final PasswordService passwordService;

    final MessageSource messageSource;

    public AuthController(AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder,
                          UserService userService,
                          EmailVerificationService emailVerificationService, PasswordService passwordService, MessageSource messageSource
    ) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.emailVerificationService = emailVerificationService;
        this.passwordService = passwordService;
        this.messageSource = messageSource;
    }

    @GetMapping("/user")
    public String user() {
        return userService.findAll().toString();
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponseDTO> login(@RequestBody LoginDto loginDTO) {
        System.out.println("AUTHENTICATING");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>(new GenericResponseDTO(201,true, "Login Successful"), HttpStatusCode.valueOf(201));
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
    public ResponseEntity<GenericResponseDTO> verifyEmail(@RequestParam String email, @RequestParam String code) {
        try {
            emailVerificationService.enableUser(email, code);
            GenericResponseDTO response = new GenericResponseDTO(201, true, "Verification Success");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(201));

        } catch (IllegalStateException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/get-verification-token")
    public ResponseEntity<GenericResponseDTO> resendVerificationToken(@RequestParam String email) {
        boolean isSend = emailVerificationService.resendVerificationEmail(email);
        if (isSend) {
            String message = messageSource.getMessage("auth.email.resend-token-success", null, LocaleContextHolder.getLocale());
            GenericResponseDTO response = new GenericResponseDTO(201, true, message);
            return new ResponseEntity<>(response, HttpStatus.valueOf(201));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/get-reset-password-link")
    public ResponseEntity<GenericResponseDTO> sendResetPasswordEmail(@RequestParam String email) {
        passwordService.sendResetPasswordEmail(email);
        String message = messageSource.getMessage("auth.password.send-link-success", null, LocaleContextHolder.getLocale());
        GenericResponseDTO response = new GenericResponseDTO(201, true, message);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(201));


    }

    @PostMapping("/reset-password")
    public ResponseEntity<GenericResponseDTO> resetPassword(@RequestBody PasswordResetDTO passwordResetDTO) {
        passwordService.resetPassword(passwordResetDTO);
        String message = messageSource.getMessage("auth.password.reset-success", null, LocaleContextHolder.getLocale());

        GenericResponseDTO response = new GenericResponseDTO(201, true, message);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(201));
    }

    @GetMapping("/is-auth")
    public ResponseEntity<Boolean> isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuth;
        isAuth = !(authentication instanceof AnonymousAuthenticationToken);
        System.out.println("USER IS " + isAuth + " " + authentication.getName());
        return ResponseEntity.ok(isAuth);

//        return ResponseEntity.ok(new GenericResponseDTO(200, true, );

    }

}
