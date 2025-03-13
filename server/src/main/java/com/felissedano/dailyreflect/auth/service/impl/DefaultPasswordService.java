package com.felissedano.dailyreflect.auth.service.impl;

import com.felissedano.dailyreflect.auth.domain.model.PasswordResetToken;
import com.felissedano.dailyreflect.auth.domain.model.User;
import com.felissedano.dailyreflect.auth.domain.repository.PasswordResetTokenRepository;
import com.felissedano.dailyreflect.auth.domain.repository.UserRepository;
import com.felissedano.dailyreflect.auth.exception.SamePasswordException;
import com.felissedano.dailyreflect.auth.exception.TokenExpiredOrInvalidException;
import com.felissedano.dailyreflect.auth.service.PasswordService;
import com.felissedano.dailyreflect.auth.service.dto.PasswordChangeDTO;
import com.felissedano.dailyreflect.auth.service.dto.PasswordResetDTO;
import com.felissedano.dailyreflect.common.service.MailService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultPasswordService implements PasswordService {

    private static final Logger log = LoggerFactory.getLogger(DefaultPasswordService.class);

    private final UserRepository userRepository;
    private final MailService mailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultPasswordService(UserRepository userRepository,
                                  MailService mailService,
                                  PasswordResetTokenRepository passwordResetTokenRepository,
                                  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void sendResetPasswordEmail(String email) {
        // Check if user even exists
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            // not throwing exceptions for security
            return;
        }

        User user = optionalUser.get();

        //Delete any previous reset token if exists
        Optional<PasswordResetToken> oldResetToken = passwordResetTokenRepository.findByUserEmail(email);
        if (oldResetToken.isPresent()) {
            log.info("Old token present");
            passwordResetTokenRepository.delete(oldResetToken.get());
        }

        String token = UUID.randomUUID().toString();


        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpirationDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)); // two hours
        passwordResetTokenRepository.save(resetToken);

        Locale locale = LocaleContextHolder.getLocale();
        log.info(locale.toString());
        System.out.println(locale);

        mailService.sendLocaleTextEmail(
                email,
                "auth.password.mail.reset-password.subject",
                "auth.password.mail.reset-password.content",
                new String[]{"https://www.example.com"}
        );
    }

    @Override
    @Transactional
    public void resetPassword(PasswordResetDTO passwordResetDTO) {
        Optional<PasswordResetToken> optionalResetToken = passwordResetTokenRepository.findByUserEmail(passwordResetDTO.email());
        if (optionalResetToken.isEmpty()) {
            throw new TokenExpiredOrInvalidException("password reset toke expired");
        }

        PasswordResetToken resetToken = optionalResetToken.get();
        if (new Date(System.currentTimeMillis()).after(resetToken.getExpirationDate())) {
            throw new TokenExpiredOrInvalidException("password reset token expired");
        }

        if (!Objects.equals(resetToken.getToken(), passwordResetDTO.token())) {
            throw new TokenExpiredOrInvalidException("password reset token expired");
        }

        String newPassword = passwordEncoder.encode(passwordResetDTO.password());
        User user = resetToken.getUser();

        if (user.getPassword().equals(newPassword)) {
            throw new SamePasswordException("Password cannot be the same as previous one");
        }

        user.setPassword(newPassword);
        userRepository.save(user);

        passwordResetTokenRepository.deleteByUserEmail(passwordResetDTO.email());
    }

    @Override
    public boolean changePassword(PasswordChangeDTO passwordChangeDTO) {
        //TODO
        throw new UnsupportedOperationException();
    }
}
