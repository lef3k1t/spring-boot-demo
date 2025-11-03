package com.example.bank.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cards", indexes = {
        @Index(name = "idx_card_account_id", columnList = "account_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_pan", nullable = false, length = 16, unique = true)
    private String pan;

    @Column(name = "card_number", nullable = false, length = 16, unique = true)
    private String number;

    @Column(nullable = false, length = 5)
    private String expiry; // "MM/yy"

    @Column(name = "is_locked", nullable = false)
    private boolean isLocked;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_card_account"))
    private Account account;
}
