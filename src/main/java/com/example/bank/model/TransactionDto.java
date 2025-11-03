package com.example.bank.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransactionDto {
    private Long id;
    private String type;
    private BigDecimal amount;
    private OffsetDateTime createdAt;
    private Long accountId;
    private Long cardId;
    private String description;
}
