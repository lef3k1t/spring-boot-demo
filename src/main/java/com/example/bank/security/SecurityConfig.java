package com.example.bank.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ✅ CSRF включён, но исключаем бизнес-запросы, чтобы не мешали Postman-тестам
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(
                                "/api/auth/register/**",   // регистрация
                                "/api/customers/**",       // CRUD клиентов
                                "/api/accounts/**",        // CRUD счетов
                                "/api/cards/**",           // карты
                                "/api/transactions/**"     // транзакции
                        )
                )

                // ✅ Правила доступа
                .authorizeHttpRequests(auth -> auth
                        // Открытая регистрация для пользователей
                        .requestMatchers(HttpMethod.POST, "/api/auth/register/user").permitAll()

                        // Регистрация админов — только с ролью ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/auth/register/admin").hasRole("ADMIN")

                        // Всё остальное требует аутентификации
                        .anyRequest().authenticated()
                )

                // ✅ Basic Auth включён (по заданию)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
