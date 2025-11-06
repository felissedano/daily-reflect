package com.felissedano.dailyreflect.profile;

import com.felissedano.dailyreflect.TestContainerConfiguration;
import com.felissedano.dailyreflect.auth.UserCreatedEvent;
import com.felissedano.dailyreflect.auth.domain.model.User;
import com.felissedano.dailyreflect.auth.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestContainerConfiguration.class)
@ActiveProfiles("test")
public class ProfileIntegrationTest {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void whenUserCreated_aNewProfileShouldBeCreated() {
        User user = new User("profileintegration", "profileintegration@test.com", "NOOP");
        userRepository.saveAndFlush(user);
        publisher.publishEvent(new UserCreatedEvent(this, user));
        assertThat(userRepository.findByEmail("profileintegration@test.com").isPresent()).isTrue();
        assertThat(profileRepository.findByUser(user).isPresent()).isTrue();
    }

    @Test
    public void whenSavingProfileWithoutUserReference_shouldFail() {
        assertThatThrownBy(() -> profileRepository.save(new Profile()))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> profileRepository.save(new Profile(new User())))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @Test
    public void whenDeletingAProfile_userShouldRemain() {
        User user = new User("profileintegration2", "profileintegration2@test.com", "NOOP");
        userRepository.save(user);
        Profile profile = new Profile(user);
        profileRepository.save(profile);
        profileRepository.delete(profile);
        assertThat(profileRepository.findByUser(user).isPresent()).isFalse();
        assertThat(userRepository.findByEmail("profileintegration2@test.com").isPresent()).isTrue();
    }

}
