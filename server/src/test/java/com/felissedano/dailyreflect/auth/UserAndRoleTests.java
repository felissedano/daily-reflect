package com.felissedano.dailyreflect.auth;

import com.felissedano.dailyreflect.TestContainerConfiguration;
import com.felissedano.dailyreflect.auth.models.Role;
import com.felissedano.dailyreflect.auth.models.User;
import com.felissedano.dailyreflect.auth.models.enums.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;

@DataJpaTest
@Import(TestContainerConfiguration.class)
//@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class UserAndRoleTests {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {

    }

    @Test
    public void whenCreateNewUserWithAdminRole_UserShouldHaveUserAndRoleShouldIncludeAdmin() {
        Role admin = new Role(RoleType.ROLE_ADMIN);
        Role user = new Role(RoleType.ROLE_USER);
        User alice = new User("alice", "email", "password");
        HashSet<Role> adminAndUser = new HashSet<>(2);
        adminAndUser.add(admin);
        adminAndUser.add(user);

        alice.setRoles(adminAndUser);

        assert(alice.getRoles().contains(admin));

//        userRepository.save(alice);



    }
}
