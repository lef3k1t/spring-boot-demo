package com.example.bank.model;

import com.example.bank.model.enums.ApplicationUserRole;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private ApplicationUserRole role; // ✅ добавляем это поле
}
