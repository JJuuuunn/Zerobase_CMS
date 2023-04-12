package com.zerobase.cms.order.domain.product;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddProductCartForm {

    private Long id;
    private Long sellerId;
    private String name;
    private String description;
    private List<ProductItem> items;

    @Builder
    public AddProductCartForm(Long id, Long sellerId, String name, String description, List<ProductItem> items) {
        this.id = id;
        this.sellerId = sellerId;
        this.name = name;
        this.description = description;
        this.items = items;
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProductItem {
        private Long id;
        private String name;
        private Integer price;
        private Integer count;

        @Builder
        public ProductItem(Long id, String name, Integer price, Integer count) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.count = count;
        }
    }
}
