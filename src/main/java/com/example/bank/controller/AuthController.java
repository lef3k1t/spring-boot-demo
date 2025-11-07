package com.example.bank.controller;

import com.example.bank.entity.ApplicationUser;
import com.example.bank.model.RegisterRequest;
import com.example.bank.repository.ApplicationUserRepository;
import com.example.bank.model.enums.ApplicationUserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ApplicationUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Регистрация обычного пользователя
    @PostMapping("/register/user")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest req) {
        validatePassword(req.getPassword());
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        ApplicationUser user = ApplicationUser.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(ApplicationUserRole.ROLE_USER)
                .isAccountExpired(false)
                .isAccountLocked(false)
                .isCredentialsExpired(false)
                .isDisabled(false)
                .build();

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    // Регистрация администратора
    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterRequest req) {
        validatePassword(req.getPassword());
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        ApplicationUser admin = ApplicationUser.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(ApplicationUserRole.ROLE_ADMIN)
                .isAccountExpired(false)
                .isAccountLocked(false)
                .isCredentialsExpired(false)
                .isDisabled(false)
                .build();

        userRepository.save(admin);
        return ResponseEntity.ok("Admin registered successfully");
    }

    // Проверка сложности пароля
    private void validatePassword(String password) {
        if (password.length() < 8 ||
                !Pattern.compile("[A-Z]").matcher(password).find() ||
                !Pattern.compile("[a-z]").matcher(password).find() ||
                !Pattern.compile("[0-9]").matcher(password).find() ||
                !Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find()) {
            throw new IllegalArgumentException("Password must contain at least 8 chars, uppercase, lowercase, number, and special symbol.");
        }
    }
}
