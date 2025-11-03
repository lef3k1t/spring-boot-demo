package com.example.bank.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateTransactionRequest {
    @NotNull
    private Long fromAccountId;
    @NotNull
    private Long toAccountId;
    @NotNull
    @Min(1)
    private Long amountMinor; // сумма в «копейках/центах»
    private String description;

    public Long getFromAccountId() { return fromAccountId; }
    public Long getToAccountId() { return toAccountId; }
    public Long getAmountMinor() { return amountMinor; }
    public String getDescription() { return description; }
    public void setFromAccountId(Long fromAccountId) { this.fromAccountId = fromAccountId; }
    public void setToAccountId(Long toAccountId) { this.toAccountId = toAccountId; }
    public void setAmountMinor(Long amountMinor) { this.amountMinor = amountMinor; }
    public void setDescription(String description) { this.description = description; }
}
