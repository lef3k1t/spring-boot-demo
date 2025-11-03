package com.example.bank.service.impl;

import com.example.bank.entity.Account;
import com.example.bank.entity.Customer;
import com.example.bank.entity.Transaction;
import com.example.bank.model.AccountDto;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.CustomerRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.service.AccountService;
import com.example.bank.util.MappingUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final MappingUtils mapper;

    // ---------- CRUD ----------

    @Override
    public List<AccountDto> getAll() {
        return accountRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public AccountDto getById(Long id) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found: " + id));
        return mapper.toDto(acc);
    }

    @Override
    public AccountDto createRaw(AccountDto dto) {
        Account entity = mapper.toEntity(dto);

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + dto.getCustomerId()));
        entity.setCustomer(customer);

        Account saved = accountRepository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public AccountDto update(Long id, AccountDto dto) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found: " + id));

        acc.setCurrency(dto.getCurrency());
        acc.setNumber(dto.getNumber());
        Account saved = accountRepository.save(acc);

        return mapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }

    // ---------- BUSINESS ----------

    @Override
    @Transactional
    public AccountDto open(Long customerId, String currency, BigDecimal initialDeposit) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + customerId));

        Account account = Account.builder()
                .customer(customer)
                .currency(currency)
                .number("ACC-" + System.currentTimeMillis())
                .balance(BigDecimal.ZERO)
                .build();

        if (initialDeposit != null && initialDeposit.compareTo(BigDecimal.ZERO) > 0) {
            account.setBalance(initialDeposit);
        }

        Account saved = accountRepository.save(account);

        if (initialDeposit != null && initialDeposit.compareTo(BigDecimal.ZERO) > 0) {
            transactionRepository.save(Transaction.builder()
                    .type("DEPOSIT")
                    .amount(initialDeposit)
                    .createdAt(OffsetDateTime.now())
                    .account(saved)
                    .description("Initial deposit")
                    .build());
        }

        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public AccountDto deposit(Long accountId, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found: " + accountId));

        acc.setBalance(acc.getBalance().add(amount));
        Account saved = accountRepository.save(acc);

        transactionRepository.save(Transaction.builder()
                .type("DEPOSIT")
                .amount(amount)
                .createdAt(OffsetDateTime.now())
                .account(saved)
                .description(description)
                .build());

        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        Account from = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new EntityNotFoundException("From account not found"));
        Account to = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new EntityNotFoundException("To account not found"));

        if (from.getBalance().compareTo(amount) < 0)
            throw new IllegalStateException("Insufficient funds");

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        accountRepository.save(from);
        accountRepository.save(to);

        OffsetDateTime now = OffsetDateTime.now();

        transactionRepository.save(Transaction.builder()
                .type("TRANSFER_OUT")
                .amount(amount)
                .createdAt(now)
                .account(from)
                .description(description)
                .build());

        transactionRepository.save(Transaction.builder()
                .type("TRANSFER_IN")
                .amount(amount)
                .createdAt(now)
                .account(to)
                .description(description)
                .build());
    }
}
