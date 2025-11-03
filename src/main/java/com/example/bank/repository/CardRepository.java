package com.example.bank.repository;

import com.example.bank.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByPan(String pan);
    boolean existsByNumber(String number);
}
