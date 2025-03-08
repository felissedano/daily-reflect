package com.felissedano.dailyreflect.auth.service.impl;

import com.felissedano.dailyreflect.auth.domain.repository.UserRepository;
import com.felissedano.dailyreflect.auth.domain.model.User;
import com.felissedano.dailyreflect.auth.exception.AlreadyVerifiedException;
import com.felissedano.dailyreflect.auth.exception.TokenExpiredException;
import com.felissedano.dailyreflect.auth.exception.TokenNotMatchException;
import com.felissedano.dailyreflect.auth.service.EmailVerificationService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class DefaultEmailVerificationService implements EmailVerificationService {
    private final UserRepository userRepository;


    public DefaultEmailVerificationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean sendVerificationEmail(String email, String username, String code) {
        //TODO send verification email with MailSender
//        throw new UnsupportedOperationException("Not implemented");
        return false;
    }

    @Override
    public boolean resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        String newCode = UUID.randomUUID().toString();
        Date expiration = new Date(System.currentTimeMillis());
        user.setVerificationCode(newCode);
        user.setCodeExpiration(expiration);
        userRepository.save(user);

        return sendVerificationEmail(email, user.getUsername(), user.getVerificationCode());
    }


    @Override
    public boolean enableUser(String email, String verificationCode) {
        User user = userRepository.findByEmail(email).orElseThrow();

        if (user.getVerificationCode() == null) {
            if (user.isEnabled()) {
                throw new AlreadyVerifiedException("User is already enabled");
            } else {
                // Some user not enabled but not verification email sent, could mean malicious activity or user account is disabled
                throw new IllegalStateException("User not enabled and with no token");
            }
        }

        if (user.getCodeExpiration().getTime() < new Date(System.currentTimeMillis()).getTime()) {
            throw new TokenExpiredException("Token is expired");
        }

        if (Objects.equals(user.getVerificationCode(), verificationCode)) {
            user.setEnabled(true);
            user.setVerificationCode(null);
            user.setEmailVerifiedAt(new Date(System.currentTimeMillis()));
            user.setCodeExpiration(null);
            userRepository.save(user);
            return true;
        }

        throw new TokenNotMatchException("The token does not match the record in our database");
    }
}
