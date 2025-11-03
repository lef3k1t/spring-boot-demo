package com.example.bank.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private Long id;
    private String number;
    private String currency;
    private BigDecimal balance;
    private Long customerId;
}
