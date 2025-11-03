package com.example.bank.service;

import com.example.bank.model.AccountDto;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    List<AccountDto> getAll();
    AccountDto getById(Long id);
    AccountDto createRaw(AccountDto dto);
    AccountDto update(Long id, AccountDto dto);
    void delete(Long id);

    // бизнес-операции
    AccountDto open(Long customerId, String currency, BigDecimal initialDeposit);
    AccountDto deposit(Long accountId, BigDecimal amount, String description);
    void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String description);
}
