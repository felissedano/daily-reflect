package com.felissedano.dailyreflect.auth.domain.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "password_reset_tokens")
public class PasswordResetToken {
        public User getUser() {
                return user;
        }

        public void setUser(User user) {
                this.user = user;
        }

        public Date getExpirationDate() {
                return expirationDate;
        }

        public void setExpirationDate(Date expirationDate) {
                this.expirationDate = expirationDate;
        }

        public String getToken() {
                return token;
        }

        public void setToken(String token) {
                this.token = token;
        }

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(name = "token", nullable = false)
        private String token;

        @Column(name = "expiration_date", nullable = false)
        private Date expirationDate;

        @OneToOne(fetch = FetchType.EAGER, targetEntity = User.class)
        @JoinColumn(name = "user_email", referencedColumnName = "email", nullable = false)
        private User user;

        @CreatedDate
        private LocalDateTime createdDate;

        @LastModifiedDate
        private LocalDateTime lastModifiedDate;
}
