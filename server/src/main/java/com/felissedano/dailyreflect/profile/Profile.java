package com.felissedano.dailyreflect.profile;

import com.felissedano.dailyreflect.auth.domain.model.User;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "profiles")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    public Profile() {}

    public Profile(User user) {
        this.user = user;
    }
}
