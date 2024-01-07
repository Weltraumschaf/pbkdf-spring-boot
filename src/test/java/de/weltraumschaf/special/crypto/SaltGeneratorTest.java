package de.weltraumschaf.special.crypto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class SaltGeneratorTest {

    private final SaltGenerator sut = new SaltGenerator();

    SaltGeneratorTest() throws NoSuchAlgorithmException {
        super();
    }

    @ParameterizedTest
    @ValueSource(ints = {-23, -3, -1, 0, 1})
    void generate_throwsErrorIfLengthIsLessThanTwo(int length) {
        assertThrows(IllegalArgumentException.class, () -> sut.generate(length));
    }

    @Test
    void generate_producesNonEmptyArrayWithGivenLength() {
        final var salt = sut.generate(16);

        assertAll(
            () -> assertThat(salt, is(not(nullValue()))),
            () -> assertThat(salt.length, is(16))
        );
    }

}
