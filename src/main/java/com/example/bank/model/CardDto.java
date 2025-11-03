package com.example.bank.model;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CardDto {
    private Long id;
    private Long accountId;
    private String pan;
    private String number;
    private String expiry; // "MM/yy"
    private Boolean isLocked;
}
