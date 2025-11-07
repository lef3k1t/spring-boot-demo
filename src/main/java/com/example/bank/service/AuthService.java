package com.example.bank.service;

import com.example.bank.entity.ApplicationUser;
import com.example.bank.model.RegisterRequest;
import com.example.bank.model.enums.ApplicationUserRole;

public interface AuthService {
    ApplicationUser register(RegisterRequest request);
}
