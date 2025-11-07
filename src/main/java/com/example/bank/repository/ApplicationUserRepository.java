package com.example.bank.repository;

import com.example.bank.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, UUID> {

    Optional<ApplicationUser> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByName(String name);
}
