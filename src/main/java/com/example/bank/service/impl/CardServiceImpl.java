package com.example.bank.service.impl;

import com.example.bank.entity.Account;
import com.example.bank.entity.Card;
import com.example.bank.entity.Transaction;
import com.example.bank.model.CardDto;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.CardRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.service.CardService;
import com.example.bank.util.MappingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
    private final MappingUtils mapping;

    @Override
    @Transactional
    public CardDto issueCard(Long accountId) {
        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));

        String pan = random16Digits();
        String number = pan; // в твоей схеме number = card_number, храним то же 16-значное значение

        Card card = Card.builder()
                .account(acc)
                .pan(pan)
                .number(number)
                .expiry(defaultExpiry())  // "MM/yy"
                .isLocked(false)
                .build();

        Card saved = cardRepository.save(card);
        return mapping.toDto(saved);
    }

    @Override
    @Transactional
    public CardDto lock(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found: " + cardId));
        if (card.isLocked()) return mapping.toDto(card);
        card.setLocked(true);
        return mapping.toDto(cardRepository.save(card));
    }

    @Override
    @Transactional
    public void payByCard(Long cardId, BigDecimal amount, String description) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be > 0");
        }

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found: " + cardId));

        if (card.isLocked()) {
            throw new IllegalStateException("Card is locked");
        }

        Account acc = card.getAccount();

        // Проверка баланса
        if (acc.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }

        // Списываем
        acc.setBalance(acc.getBalance().subtract(amount));
        accountRepository.save(acc);

        // Логируем транзакцию
        Transaction tx = Transaction.builder()
                .type("CARD_PAYMENT")
                .amount(amount)
                .createdAt(OffsetDateTime.now())
                .account(acc)
                .card(card)
                .description(description != null ? description : "Card payment")
                .build();
        transactionRepository.save(tx);
    }

    /* ------------ helpers ------------ */

    private String random16Digits() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) sb.append(r.nextInt(10));
        return sb.toString();
    }

    private String defaultExpiry() {
        // простой фиксированный пример; можешь генерить из LocalDate.now().plusYears(3)
        return "11/28";
    }
}
