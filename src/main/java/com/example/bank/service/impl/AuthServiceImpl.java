package com.example.bank.service.impl;

import com.example.bank.entity.ApplicationUser;
import com.example.bank.model.RegisterRequest;
import com.example.bank.model.enums.ApplicationUserRole;
import com.example.bank.repository.ApplicationUserRepository;
import com.example.bank.service.AuthService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ApplicationUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApplicationUser register(RegisterRequest request) {
        // email / name уникальны
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email already registered");
        }
        if (userRepository.existsByName(request.getName())) {
            throw new ValidationException("Username already taken");
        }

        // проверка сложности пароля
        if (!isStrongPassword(request.getPassword())) {
            throw new ValidationException(
                    "Weak password. Must be >=8 chars, with upper, lower, digit and special symbol."
            );
        }

        // роль по умолчанию — USER, если не передана
        var role = request.getRole() != null ? request.getRole() : ApplicationUserRole.ROLE_USER;

        var user = ApplicationUser.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .isAccountExpired(false)
                .isAccountLocked(false)
                .isCredentialsExpired(false)
                .isDisabled(false)
                .build();

        return userRepository.save(user);
    }

    private boolean isStrongPassword(String pwd) {
        if (pwd == null || pwd.length() < 8) return false;
        boolean upper = false, lower = false, digit = false, special = false;

        for (char c : pwd.toCharArray()) {
            if (Character.isUpperCase(c)) upper = true;
            else if (Character.isLowerCase(c)) lower = true;
            else if (Character.isDigit(c)) digit = true;
            else special = true;
        }
        return upper && lower && digit && special;
    }
}
