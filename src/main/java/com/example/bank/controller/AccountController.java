package com.example.bank.controller;

import com.example.bank.model.AccountDto;
import com.example.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // ---------- CRUD ----------

    @GetMapping
    public List<AccountDto> getAll() {
        return accountService.getAll();
    }

    @GetMapping("/{id}")
    public AccountDto getById(@PathVariable Long id) {
        return accountService.getById(id);
    }

    @PostMapping
    public AccountDto create(@RequestBody AccountDto dto) {
        return accountService.createRaw(dto);
    }

    @PutMapping("/{id}")
    public AccountDto update(@PathVariable Long id, @RequestBody AccountDto dto) {
        return accountService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        accountService.delete(id);
    }

    // ---------- BUSINESS ----------

    @PostMapping("/open")
    public AccountDto open(
            @RequestParam Long customerId,
            @RequestParam String currency,
            @RequestParam(required = false, defaultValue = "0") BigDecimal initialDeposit
    ) {
        return accountService.open(customerId, currency, initialDeposit);
    }

    @PatchMapping("/{id}/deposit")
    public AccountDto deposit(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description
    ) {
        return accountService.deposit(id, amount, description);
    }

    @PostMapping("/transfer")
    public void transfer(
            @RequestParam Long fromAccountId,
            @RequestParam Long toAccountId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description
    ) {
        accountService.transfer(fromAccountId, toAccountId, amount, description);
    }
}
