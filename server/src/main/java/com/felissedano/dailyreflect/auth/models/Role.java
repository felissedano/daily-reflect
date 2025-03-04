package com.felissedano.dailyreflect.auth.models;

import com.felissedano.dailyreflect.auth.models.enums.RoleType;
import jakarta.persistence.*;

@Entity
@Table( name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private RoleType name;

    public Role(RoleType name) {
        this.name = name;
    }

    public Role() {

    }

    public long getId() {
        return id;
    }

    public RoleType getName() {
        return name;
    }

    public void setName(RoleType name) {
        this.name = name;
    }
}
