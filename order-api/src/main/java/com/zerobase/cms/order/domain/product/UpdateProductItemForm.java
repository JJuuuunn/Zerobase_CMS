package com.zerobase.cms.order.domain.product;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateProductItemForm {

    private Long id;
    private Long productId;
    private String name;
    private Integer price;
    private Integer count;

    @Builder
    public UpdateProductItemForm(Long id, Long productId, String name, Integer price, Integer count) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.count = count;
    }
}
