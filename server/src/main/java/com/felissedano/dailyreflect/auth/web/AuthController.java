package com.felissedano.dailyreflect.auth.web;

import com.felissedano.dailyreflect.auth.service.PasswordService;
import com.felissedano.dailyreflect.auth.service.dto.LoginDto;
import com.felissedano.dailyreflect.auth.service.dto.PasswordResetDTO;
import com.felissedano.dailyreflect.auth.service.dto.UserDto;
import com.felissedano.dailyreflect.auth.domain.model.User;
import com.felissedano.dailyreflect.auth.service.EmailVerificationService;
import com.felissedano.dailyreflect.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
    public ResponseEntity<String> login(@RequestBody LoginDto loginDTO) {
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

        return new ResponseEntity<>("Login Successful", HttpStatusCode.valueOf(201));
    }

    //TODO: maybe move this endpoint to UserController as user need to login to logout
    @DeleteMapping("/logout")
    public ResponseEntity<String> logout() {
//        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout Successfullll");
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

    @GetMapping("/get-verification-token")
    public ResponseEntity<String> resendVerificationToken(@RequestParam String email) {
        //TODO implement it
        boolean isSend = emailVerificationService.resendVerificationEmail(email);
        if (isSend) {
            String message = messageSource.getMessage("auth.email.resend-token-success", null, LocaleContextHolder.getLocale());
            return new ResponseEntity<>(message, HttpStatus.valueOf(201));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/get-reset-password-link")
    public ResponseEntity<String> sendResetPasswordEmail(@RequestParam String email) {

        passwordService.sendResetPasswordEmail(email);
        String message = messageSource.getMessage("auth.password.send-link-success", null, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(message, HttpStatusCode.valueOf(201));


    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetDTO passwordResetDTO) {
        passwordService.resetPassword(passwordResetDTO);
        String message = messageSource.getMessage("auth.password.reset-success", null, LocaleContextHolder.getLocale());

        return new ResponseEntity<>(message, HttpStatus.valueOf(201));
    }

}
