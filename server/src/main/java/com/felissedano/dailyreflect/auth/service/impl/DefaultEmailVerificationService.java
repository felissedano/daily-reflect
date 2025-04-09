package com.felissedano.dailyreflect.auth.service.impl;

import com.felissedano.dailyreflect.auth.AuthUtils;
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
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Service
public class DefaultEmailVerificationService implements EmailVerificationService {
    private static final Logger log = LoggerFactory.getLogger(DefaultEmailVerificationService.class);

    private final MailService mailService;
    private final UserRepository userRepository;
    private final Environment env;


    public DefaultEmailVerificationService(MailService mailService, UserRepository userRepository, Environment environment) {
        this.mailService = mailService;
        this.userRepository = userRepository;
        this.env = environment;
    }

    @Override
    public boolean sendVerificationEmail(String email, String username, String code) {
        Locale locale = LocaleContextHolder.getLocale();
        String url = env.getProperty("app.client-url") + "/auth/verify/user/email?email=" + email + "&code=" + code;
        Object[] args = {username, url};
        return mailService.sendLocaleTextEmail(email, "auth.email.verify-with-link.subject", "auth.email.verify-with-link.content", args);
    }

    @Override
    @Transactional
    public boolean resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new BadEmailVerificationRequestException(""));
        if (user.getEmailVerifiedAt() != null) {
            throw new BadEmailVerificationRequestException("");
        }
        String newToken = AuthUtils.generateVerificationToken();
        Date expiration = AuthUtils.generateTokenExpirationDate();
        user.setVerificationCode(newToken);
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
