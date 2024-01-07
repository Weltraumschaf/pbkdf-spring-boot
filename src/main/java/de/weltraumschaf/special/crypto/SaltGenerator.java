package de.weltraumschaf.special.crypto;

import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class SaltGenerator {
    private final SecureRandom random = SecureRandom.getInstanceStrong();

    public SaltGenerator() throws NoSuchAlgorithmException {
        super();
    }

    public byte[] generate(int length) {
        if (length < 2) {
            throw new IllegalArgumentException("Length must be greater than one!");
        }

        return random.generateSeed(length);
    }

}
