package com.example.bank.entity;

import com.example.bank.model.enums.ApplicationUserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_users_name", columnNames = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationUser {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(nullable = false, length = 128)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ApplicationUserRole role;

    @Column(nullable = false)
    private boolean isAccountExpired;

    @Column(nullable = false)
    private boolean isAccountLocked;

    @Column(nullable = false)
    private boolean isCredentialsExpired;

    @Column(nullable = false)
    private boolean isDisabled;
}
