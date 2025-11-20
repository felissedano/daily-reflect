package com.felissedano.dailyreflect.auth.domain.repository;

import com.felissedano.dailyreflect.auth.domain.model.PasswordResetToken;
import com.felissedano.dailyreflect.auth.domain.model.User;
import jakarta.validation.constraints.Email;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    List<PasswordResetToken> findByExpirationDateBefore(Date expirationDateBefore);

    Optional<PasswordResetToken> findByUser(User user);

    Optional<PasswordResetToken> findByUserEmail(@Email(message = "Email in valid") String userEmail);

    void deleteByUserEmail(@Email(message = "Email invalid") String userEmail);
}
