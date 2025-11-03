package com.example.bank.service;

import com.example.bank.model.CustomerDto;
import java.util.List;

public interface CustomerService {
    List<CustomerDto> getAll();
    CustomerDto getById(Long id);
    CustomerDto create(CustomerDto dto);
    CustomerDto update(Long id, CustomerDto dto);
    void delete(Long id);
}
