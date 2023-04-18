package com.zerobase.cms.order.client.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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
}
