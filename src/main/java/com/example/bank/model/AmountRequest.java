package com.example.bank.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Сумма в минимальных единицах (копейках/центах) */
public class AmountRequest {
    @NotNull
    @Min(1)
    private Long amountMinor;

    public Long getAmountMinor() { return amountMinor; }
    public void setAmountMinor(Long amountMinor) { this.amountMinor = amountMinor; }
}
