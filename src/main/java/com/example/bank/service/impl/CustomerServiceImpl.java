package com.example.bank.service.impl;

import com.example.bank.entity.Customer;
import com.example.bank.exception.BadRequestException;
import com.example.bank.exception.NotFoundException;
import com.example.bank.model.CustomerDto;
import com.example.bank.repository.CustomerRepository;
import com.example.bank.service.CustomerService;
import com.example.bank.util.MappingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final MappingUtils mapping;

    @Override
    public List<CustomerDto> getAll() {
        return repository.findAll().stream()
                .map(mapping::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDto getById(Long id) {
        Customer c = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer " + id + " not found"));
        return mapping.toDto(c);
    }

    @Override
    public CustomerDto create(CustomerDto dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already used");
        }
        Customer saved = repository.save(mapping.toEntity(dto));
        return mapping.toDto(saved);
    }

    @Override
    public CustomerDto update(Long id, CustomerDto dto) {
        Customer c = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer " + id + " not found"));
        if (!c.getEmail().equals(dto.getEmail()) && repository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already used");
        }
        c.setFirstName(dto.getFirstName());
        c.setLastName(dto.getLastName());
        c.setEmail(dto.getEmail());
        return mapping.toDto(repository.save(c));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
