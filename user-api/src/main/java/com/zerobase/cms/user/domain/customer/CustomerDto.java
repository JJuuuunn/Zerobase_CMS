package com.zerobase.cms.user.domain.customer;

import com.zerobase.cms.user.domain.model.Customer;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
public class CustomerDto {

    private Long id;
    private String email;
    private Integer balance;

    @Builder
    public CustomerDto(Long id, String email, Integer balance) {
        this.id = id;
        this.email = email;
        this.balance = balance;
    }

    public static CustomerDto from(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .balance(Optional.ofNullable(customer.getBalance()).orElse(0))
                .build();
    }
}
