package com.felissedano.dailyreflect.auth.service.impl;

import com.felissedano.dailyreflect.auth.domain.repository.UserRepository;
import com.felissedano.dailyreflect.auth.domain.model.User;
import com.felissedano.dailyreflect.auth.exception.BadEmailVerificationRequestException;
import com.felissedano.dailyreflect.auth.exception.EmailAlreadyVerifiedException;
import com.felissedano.dailyreflect.auth.exception.TokenExpiredOrInvalidException;
import com.felissedano.dailyreflect.auth.service.EmailVerificationService;
import com.felissedano.dailyreflect.common.service.MailService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Service
public class DefaultEmailVerificationService implements EmailVerificationService {
    private static final Logger log = LoggerFactory.getLogger(DefaultEmailVerificationService.class);

    private final MailService mailService;
    private final UserRepository userRepository;


    public DefaultEmailVerificationService(MailService mailService, UserRepository userRepository) {
        this.mailService = mailService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean sendVerificationEmail(String email, String username, String code) {
        Locale locale = LocaleContextHolder.getLocale();
        Object[] args = {username, code};
        return mailService.sendTextEmail(email, "auth.email.verify-with-link.subject", "auth.email.verify-with-link.content", args, locale);
    }

    @Override
    @Transactional
    public boolean resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new BadEmailVerificationRequestException(""));
        if (user.getEmailVerifiedAt() != null) {
            throw new BadEmailVerificationRequestException("");
        }
        String newCode = UUID.randomUUID().toString();
        Date expiration = new Date(System.currentTimeMillis());
        user.setVerificationCode(newCode);
        user.setCodeExpiration(expiration);
        userRepository.save(user);

        return sendVerificationEmail(email, user.getUsername(), user.getVerificationCode());
    }


    @Override
    public void enableUser(String email, String verificationCode) {

        User user = userRepository.findByEmail(email).orElseThrow();

        if (Objects.equals(user.getVerificationCode(), verificationCode)) {
            if (new Date(System.currentTimeMillis()).after(user.getCodeExpiration())) {
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
        } else {
            throw new TokenExpiredOrInvalidException("Email Validation Error");
        }

    }

}
