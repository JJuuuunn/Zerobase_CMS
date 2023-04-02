package com.zerobase.cms.user.domain.customer;

import com.zerobase.cms.user.domain.model.Customer;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomerDto {

    private Long id;
    private String email;

    @Builder
    public CustomerDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public static CustomerDto from(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .build();
    }
}
