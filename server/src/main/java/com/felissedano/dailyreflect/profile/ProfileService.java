package com.felissedano.dailyreflect.profile;

import com.felissedano.dailyreflect.auth.UserCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @EventListener(UserCreatedEvent.class)
    public void createProfileOnUserEmailVerified(UserCreatedEvent event) {
        Profile profile = new Profile(event.getUser());
        profileRepository.save(profile);
    }
}
