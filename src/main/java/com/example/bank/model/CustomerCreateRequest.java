package com.example.bank.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Входная модель для создания клиента.
 * ВАЖНО: публичный no-args + сеттеры нужны Jackson для десериализации.
 */
@Getter
@Setter
@NoArgsConstructor
public class CustomerCreateRequest {

    @NotBlank(message = "firstName is required")
    private String firstName;

    @NotBlank(message = "lastName is required")
    private String lastName;

    @Email(message = "email must be valid")
    @NotBlank(message = "email is required")
    private String email;
}
