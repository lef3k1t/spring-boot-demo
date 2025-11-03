package com.example.bank.controller;

import com.example.bank.model.TransactionDto;
import com.example.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @GetMapping public List<TransactionDto> all(){ return service.getAll(); }
    @GetMapping("/{id}") public TransactionDto byId(@PathVariable Long id){ return service.getById(id); }
}
