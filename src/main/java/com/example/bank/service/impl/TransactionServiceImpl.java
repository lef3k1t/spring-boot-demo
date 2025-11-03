package com.example.bank.service.impl;

import com.example.bank.exception.NotFoundException;
import com.example.bank.model.TransactionDto;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.service.TransactionService;
import com.example.bank.util.MappingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository repo;
    private final MappingUtils mapping;

    @Override
    public List<TransactionDto> getAll() {
        return repo.findAll().stream()
                .map(mapping::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDto getById(Long id) {
        return mapping.toDto(
                repo.findById(id).orElseThrow(() -> new NotFoundException("Tx " + id + " not found"))
        );
    }
}
