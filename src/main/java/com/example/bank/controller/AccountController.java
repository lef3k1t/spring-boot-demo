package com.example.bank.controller;

import com.example.bank.model.AccountDto;
import com.example.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // ---------- CRUD ----------

    /**
     * Получить все счета
     * Доступ: только ADMIN
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AccountDto> getAll() {
        return accountService.getAll();
    }

    /**
     * Получить счёт по id
     * Доступ: ADMIN и USER
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public AccountDto getById(@PathVariable Long id) {
        return accountService.getById(id);
    }

    /**
     * Техническое создание счёта (raw)
     * Доступ: только ADMIN
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public AccountDto create(@RequestBody AccountDto dto) {
        return accountService.createRaw(dto);
    }

    /**
     * Обновить счёт
     * Доступ: только ADMIN
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AccountDto update(@PathVariable Long id, @RequestBody AccountDto dto) {
        return accountService.update(id, dto);
    }

    /**
     * Удалить счёт
     * Доступ: только ADMIN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        accountService.delete(id);
    }

    // ---------- BUSINESS ----------

    /**
     * Открыть счёт клиенту
     * Доступ: ADMIN и USER
     */
    @PostMapping("/open")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public AccountDto open(
            @RequestParam Long customerId,
            @RequestParam String currency,
            @RequestParam(required = false, defaultValue = "0") BigDecimal initialDeposit
    ) {
        return accountService.open(customerId, currency, initialDeposit);
    }

    /**
     * Пополнение счёта
     * Доступ: ADMIN и USER
     */
    @PatchMapping("/{id}/deposit")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public AccountDto deposit(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description
    ) {
        return accountService.deposit(id, amount, description);
    }

    /**
     * Перевод между счетами
     * Доступ: ADMIN и USER
     */
    @PostMapping("/transfer")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public void transfer(
            @RequestParam Long fromAccountId,
            @RequestParam Long toAccountId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description
    ) {
        accountService.transfer(fromAccountId, toAccountId, amount, description);
    }
}
