package com.felissedano.dailyreflect.auth.service.impl;

import com.felissedano.dailyreflect.auth.domain.repository.UserRepository;
import com.felissedano.dailyreflect.auth.domain.model.User;
import com.felissedano.dailyreflect.auth.exception.EmailAlreadyVerifiedException;
import com.felissedano.dailyreflect.auth.exception.TokenExpiredOrInvalidException;
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

        if (Objects.equals(user.getVerificationCode(), verificationCode)) {
            if (user.getCodeExpiration().getTime() < new Date(System.currentTimeMillis()).getTime()) {
                throw new TokenExpiredOrInvalidException("Email Validation Error");
            }

            if (user.getEmailVerifiedAt() != null) {
                throw new EmailAlreadyVerifiedException("Email Validation Error");
            }

            user.setEnabled(true);
            user.setVerificationCode(null);
            user.setEmailVerifiedAt(new Date(System.currentTimeMillis()));
            user.setCodeExpiration(null);
            userRepository.save(user);
            return true;
        } else {
            throw new TokenExpiredOrInvalidException("Email Validation Error");
        }

    }

}
