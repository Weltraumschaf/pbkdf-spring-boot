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

    @Size(max = 255)
    @Column(name = "username")
    private String username;

    @Size(max = 255)
    @Column(name = "password", nullable = false)
    private String password;

}
