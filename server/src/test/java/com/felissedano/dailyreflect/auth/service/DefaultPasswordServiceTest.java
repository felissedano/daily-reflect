package com.felissedano.dailyreflect.auth.service;

import com.felissedano.dailyreflect.auth.domain.model.PasswordResetToken;
import com.felissedano.dailyreflect.auth.domain.model.User;
import com.felissedano.dailyreflect.auth.domain.repository.PasswordResetTokenRepository;
import com.felissedano.dailyreflect.auth.domain.repository.UserRepository;
import com.felissedano.dailyreflect.auth.service.impl.DefaultPasswordService;
import com.felissedano.dailyreflect.common.service.MailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultPasswordServiceTest {

    @Mock
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    MailService mailService;

    @Mock
    Environment env;

    @InjectMocks
    DefaultPasswordService passwordService;


    @Test
    void whenSendResetEmailToExistingUser_shouldSendWithSuccess() {
        // given
        User john = new User("john", "john@example.com", "password");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(john));

        // when
        passwordService.sendResetPasswordEmail("john@example.com");

        // then
        verify(passwordResetTokenRepository, times(1)).save(any());
        verify(mailService, times(1)).sendLocaleTextEmail(eq("john@example.com"),anyString(),anyString(),any());
    }

    @Test
    void whenSendResetEmailToNonExistingUser_shouldNotDoAnything() {
        // given
        Optional<User> empty = Optional.empty();
        when(userRepository.findByEmail("fake@example.com")).thenReturn(empty);

        // when
        passwordService.sendResetPasswordEmail("fake@example.com");

        // then
        verify(passwordResetTokenRepository, never()).findByUserEmail(anyString());
        verify(mailService, never()).sendLocaleTextEmail(any(),any(),any(),any());

    }

    @Test
    void whenSendResetEmailWithExistingToken_shouldReplaceOldTokenWithNewToken() {
        // given
        User john = new User("john", "john@example.com", "password");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(john));

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(john);
        when(passwordResetTokenRepository.findByUserEmail("john@example.com")).thenReturn(Optional.of(resetToken));

        // when
        passwordService.sendResetPasswordEmail("john@example.com");

        // then
        verify(passwordResetTokenRepository, times(1)).delete(resetToken);
        verify(passwordResetTokenRepository, times(1)).save(any(PasswordResetToken.class));

    }


}