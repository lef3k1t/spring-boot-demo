package com.example.bank.service;

import com.example.bank.model.CardDto;

import java.math.BigDecimal;

public interface CardService {
    CardDto issueCard(Long accountId);
    CardDto lock(Long cardId);

    void    payByCard(Long cardId, BigDecimal amount, String description);
}
