package com.example.bank.controller;

import com.example.bank.model.CardDto;
import com.example.bank.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cards") // baseUrl = http://localhost:8080/api
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    /**
     * Выпуск карты для счёта.
     * Доступ: только ADMIN.
     */
    @PostMapping("/issue")
    @PreAuthorize("hasRole('ADMIN')")
    public CardDto issue(@RequestParam("accountId") Long accountId) {
        return cardService.issueCard(accountId);
    }

    /**
     * Блокировка карты.
     * Доступ: только ADMIN.
     */
    @PatchMapping("/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public CardDto lock(@RequestParam("cardId") Long cardId) {
        return cardService.lock(cardId);
    }

    /**
     * Оплата картой.
     * Доступ: ADMIN и USER.
     */
    @PostMapping("/{cardId}/pay")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public void payByCard(@PathVariable("cardId") Long cardId,
                          @RequestParam("amount") BigDecimal amount,
                          @RequestParam(value = "description", required = false) String description) {
        cardService.payByCard(cardId, amount, description);
    }
}
