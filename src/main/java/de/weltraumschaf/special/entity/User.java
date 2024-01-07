package de.weltraumschaf.special.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "users")
public class User {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Size(max = 256)
    @Column(name = "username")
    private String username;

    @Size(max = 256)
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 256)
    @Column(name = "encryption_salt", nullable = false)
    private String encryptionSalt;

    @Size(max = 64)
    @Column(name = "encryption_key")
    private String encryptionKey;
}
