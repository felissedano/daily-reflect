package com.felissedano.dailyreflect.auth.service.impl;

import com.felissedano.dailyreflect.auth.domain.repository.UserRepository;
import com.felissedano.dailyreflect.auth.service.PasswordService;
import com.felissedano.dailyreflect.auth.service.dto.PasswordChangeDTO;
import com.felissedano.dailyreflect.auth.service.dto.PasswordResetDTO;
import com.felissedano.dailyreflect.common.service.MailService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class DefaultPasswordService implements PasswordService {
    private final UserRepository userRepository;
    private final MailService mailService;
    private final MessageSource messageSource;

    public DefaultPasswordService(UserRepository userRepository, MailService mailService, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.messageSource = messageSource;
    }

    @Override
    public boolean sendResetPasswordEmail(String email) {
        //TODO
        return false;
    }

    @Override
    public boolean resetPassword(PasswordResetDTO passwordResetDTO) {
        //TODO
        return false;
    }

    @Override
    public boolean changePassword(PasswordChangeDTO passwordChangeDTO) {
        //TODO
        return false;
    }
}
