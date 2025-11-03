package com.example.bank.model;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
