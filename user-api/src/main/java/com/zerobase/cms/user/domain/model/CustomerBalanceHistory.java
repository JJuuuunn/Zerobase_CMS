package com.zerobase.cms.user.domain.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerBalanceHistory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Customer.class, fetch = FetchType.LAZY)
    private Customer customer;
    private Integer changeMoney; // 변경된 돈
    private Integer currentMoney; // 해당 시점 잔액
    private String fromMessage;
    private String description;

    @Builder
    public CustomerBalanceHistory(Customer customer, Integer changeMoney, Integer currentMoney, String fromMessage, String description) {
        this.customer = customer;
        this.changeMoney = changeMoney;
        this.currentMoney = currentMoney;
        this.fromMessage = fromMessage;
        this.description = description;
    }
}
