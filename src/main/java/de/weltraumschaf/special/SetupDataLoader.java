package de.weltraumschaf.special;

import de.weltraumschaf.special.crypto.SaltGenerator;
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

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static final int ITERATION_COUNT = 250_000;
    private static final int KEY_LENGTH = 256;

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

        final var user = createDefaultUser();
        users.save(user);

        alreadySetup = true;
    }

    private User createDefaultUser() {
        try {
            /*
             * https://howtodoinjava.com/junit5/jacoco-test-coverage/
             * https://docs.oracle.com/en/java/javase/11/docs/specs/security/standard-names.html
             * https://security.stackexchange.com/questions/179204/using-pbkdf2-for-hash-and-aes-key-generation-implementation
             */
            final var salts = new SaltGenerator();
            // FIXME: Generate random password and print to log.
            final var plaintextPassword = "test1234";
            final var salt = salts.generate(128);
            final var derivedKey = deriveKeyFromPassword(plaintextPassword, salt);
            final var randomKey = generateRandomKey();

            log.info(">>>>>>>> DERIVED KEY: {}", DatatypeConverter.printHexBinary(derivedKey.getEncoded()));
            log.info(">>>>>>>> RANDOM KEY: {}", DatatypeConverter.printHexBinary(randomKey.getEncoded()));

            final var user = new User();
            user.setUsername("sxs");
            user.setPassword(encoder.encode(plaintextPassword));
            user.setEncryptionSalt(DatatypeConverter.printHexBinary(salt));
            user.setEncryptionKey(DatatypeConverter.printHexBinary(encrypt(derivedKey, randomKey)));
            return user;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }


    private SecretKey deriveKeyFromPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        final var spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);

        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    private SecretKey generateRandomKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(KEY_LENGTH);
        return keyGenerator.generateKey();
    }

    private byte[] encrypt(SecretKey derivedKey, SecretKey randomKey) {
        // FIXME: Encrypt random key with derived key:
        return randomKey.getEncoded();
    }
}
