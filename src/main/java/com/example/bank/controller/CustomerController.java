package com.example.bank.controller;

import com.example.bank.model.CustomerDto;
import com.example.bank.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    /**
     * Доступ: ADMIN и USER
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<CustomerDto> all() {
        return service.getAll();
    }

    /**
     * Доступ: ADMIN и USER
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public CustomerDto byId(@PathVariable Long id) {
        return service.getById(id);
    }

    /**
     * Создать клиента
     * Доступ: только ADMIN
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerDto> create(@RequestBody CustomerDto dto) {
        CustomerDto created = service.create(dto);
        return ResponseEntity
                .created(URI.create("/api/customers/" + created.getId()))
                .body(created);
    }

    /**
     * Обновить клиента
     * Доступ: только ADMIN
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CustomerDto update(@PathVariable Long id, @RequestBody CustomerDto dto) {
        return service.update(id, dto);
    }

    /**
     * Удалить клиента
     * Доступ: только ADMIN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
