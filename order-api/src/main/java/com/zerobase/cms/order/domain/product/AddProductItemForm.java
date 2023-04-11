package com.zerobase.cms.order.domain.product;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddProductItemForm {

    private Long productId;
    private String name;
    private Integer price;
    private Integer count;

    @Builder
    public AddProductItemForm(Long productId, String name, Integer price, Integer count) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.count = count;
    }
}
