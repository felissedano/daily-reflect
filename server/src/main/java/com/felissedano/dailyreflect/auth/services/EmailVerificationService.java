package com.felissedano.dailyreflect.auth.services;

import com.felissedano.dailyreflect.auth.repositories.UserRepository;
import com.felissedano.dailyreflect.auth.models.User;
import com.felissedano.dailyreflect.auth.models.enums.VerificationState;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class EmailVerificationService {
    private final UserRepository userRepository;


    public EmailVerificationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean sendVerificationEmail(String email, String username, String code) {
        //TODO send verification email with MailSender
//        throw new UnsupportedOperationException("Not implemented");
        return false;
    }

    public boolean resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        String newCode = UUID.randomUUID().toString();
        Date expiration = new Date(System.currentTimeMillis());
        user.setVerificationCode(newCode);
        user.setCodeExpiration(expiration);
        userRepository.save(user);

        return sendVerificationEmail(email, user.getUsername(), user.getVerificationCode());
    }


    public VerificationState enableUser(String email, String verificationCode) {
        User user = userRepository.findByEmail(email).orElseThrow();

        if (user.getVerificationCode() == null) {
            if (user.isEnabled()) {
                return VerificationState.ALREADY_VERIFIED;
            } else {
                // Some user not enabled but not verification email sent, could mean malicious activity or user account is disabled
                throw new IllegalStateException("User not enabled and with no token");
            }
        }

        if (user.getCodeExpiration().getTime() < new Date(System.currentTimeMillis()).getTime()) {
            return VerificationState.TOKEN_EXPIRED;
        }

        if (Objects.equals(user.getVerificationCode(), verificationCode)) {
            user.setEnabled(true);
            user.setVerificationCode(null);
            user.setEmailVerifiedAt(new Date(System.currentTimeMillis()));
            user.setCodeExpiration(null);
            userRepository.save(user);
            return VerificationState.VERIFICATION_SUCCESS;
        }

        return VerificationState.TOKEN_NOT_MATCH;
    }
}
