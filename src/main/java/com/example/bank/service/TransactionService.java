
package com.example.bank.service;

import com.example.bank.model.TransactionDto;
import java.util.List;

public interface TransactionService {
    List<TransactionDto> getAll();
    TransactionDto getById(Long id);
}
