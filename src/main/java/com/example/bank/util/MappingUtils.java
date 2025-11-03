package com.example.bank.util;

import com.example.bank.entity.*;
import com.example.bank.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class MappingUtils {

    private static <T> T map(Object src, Class<T> target) {
        if (src == null) return null;
        try {
            T t = target.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(src, t);
            return t;
        } catch (Exception e) {
            throw new IllegalStateException("Mapping error " + src.getClass() + " -> " + target, e);
        }
    }

    public Customer toEntity(CustomerDto d){ return map(d, Customer.class); }
    public Account toEntity(AccountDto d){ return map(d, Account.class); }
    public Card toEntity(CardDto d){ return map(d, Card.class); }
    public Transaction toEntity(TransactionDto d){ return map(d, Transaction.class); }

    public CustomerDto toDto(Customer e){ return map(e, CustomerDto.class); }
    public AccountDto toDto(Account e){
        AccountDto dto = map(e, AccountDto.class);
        if (e != null && e.getCustomer()!=null) dto.setCustomerId(e.getCustomer().getId());
        return dto;
    }
    public CardDto toDto(Card e){
        CardDto dto = map(e, CardDto.class);
        if (e != null && e.getAccount()!=null) dto.setAccountId(e.getAccount().getId());
        return dto;
    }
    public TransactionDto toDto(Transaction e){
        TransactionDto dto = map(e, TransactionDto.class);
        if (e != null) {
            if (e.getAccount()!=null) dto.setAccountId(e.getAccount().getId());
            if (e.getCard()!=null) dto.setCardId(e.getCard().getId());
        }
        return dto;
    }
}
