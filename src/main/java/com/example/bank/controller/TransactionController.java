package com.example.bank.controller;

import com.example.bank.model.TransactionDto;
import com.example.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    /**
     * Получить все транзакции.
     * Доступ: только ADMIN.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<TransactionDto> all() {
        return service.getAll();
    }

    /**
     * Получить транзакцию по ID.
     * Доступ: ADMIN и USER.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public TransactionDto byId(@PathVariable Long id) {
        return service.getById(id);
    }
}
