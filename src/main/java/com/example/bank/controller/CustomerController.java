package com.example.bank.controller;

import com.example.bank.model.CustomerDto;
import com.example.bank.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @GetMapping public List<CustomerDto> all(){ return service.getAll(); }
    @GetMapping("/{id}") public CustomerDto byId(@PathVariable Long id){ return service.getById(id); }

    @PostMapping
    public ResponseEntity<CustomerDto> create(@RequestBody CustomerDto dto){
        CustomerDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/customers/"+created.getId())).body(created);
    }

    @PutMapping("/{id}") public CustomerDto update(@PathVariable Long id, @RequestBody CustomerDto dto){
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Long id){ service.delete(id); }
}
