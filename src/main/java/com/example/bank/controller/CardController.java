package com.example.bank.controller;

import com.example.bank.model.CardDto;
import com.example.bank.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.example.bank.model.CardDto;
import com.example.bank.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cards") // baseUrl = http://localhost:8080/api
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;


    // Бизнес: выпуск карты для счёта
    @PostMapping("/issue")
    public CardDto issue(@RequestParam("accountId") Long accountId) {
        return cardService.issueCard(accountId);
    }

    // Бизнес: блокировка карты
    @PatchMapping("/lock")
    public CardDto lock(@RequestParam("cardId") Long cardId) {
        return cardService.lock(cardId);
    }

    // Бизнес: оплата картой
    // ВАЖНО: путь именно /api/cards/{cardId}/pay
    @PostMapping("/{cardId}/pay")
    public void payByCard(@PathVariable("cardId") Long cardId,
                          @RequestParam("amount") BigDecimal amount,
                          @RequestParam(value = "description", required = false) String description) {
        cardService.payByCard(cardId, amount, description);
    }
}
