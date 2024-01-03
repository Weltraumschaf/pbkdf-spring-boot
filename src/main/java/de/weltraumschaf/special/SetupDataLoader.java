package de.weltraumschaf.special;

import de.weltraumschaf.special.entity.User;
import de.weltraumschaf.special.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * Event may be triggered multiple times, so ensure only one run.
     */
    private boolean alreadySetup = false;

    private final UserRepository users;

    private final PasswordEncoder encoder;

    @Autowired
    public SetupDataLoader(@NonNull UserRepository users, @NonNull PasswordEncoder encoder) {
        super();
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            log.info("Setup data loader already executed. Skipping!");
            return;
        }

        if (users.findByUsername("sxs").isPresent()) {
            log.info("Test user already exists. Skipping!");
            return;
        }

        final var user = new User();
        user.setUsername("sxs");
        user.setPassword(encoder.encode("test1234"));
        users.save(user);

        alreadySetup = true;
    }

}
