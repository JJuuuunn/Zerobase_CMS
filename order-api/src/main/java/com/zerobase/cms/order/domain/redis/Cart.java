package com.zerobase.cms.order.domain.redis;

import com.zerobase.cms.order.domain.product.AddProductCartForm;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("basket")
public class Cart {

    @Id
    private Long customerId;
    private List<Product> products = new ArrayList<>();
    private List<String> messages = new ArrayList<>();

    public void addMessage(String message) {
        messages.add(message);
    }

    @Builder
    public Cart(Long customerId, List<Product> products, List<String> messages) {
        this.customerId = customerId;
        this.products = products;
        this.messages = messages;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Product {
        private Long id;
        private Long sellerId;
        private String name;
        private String description;
        private List<ProductItem> items = new ArrayList<>();

        @Builder
        public Product(Long id, Long sellerId, String name, String description, List<ProductItem> items) {
            this.id = id;
            this.sellerId = sellerId;
            this.name = name;
            this.description = description;
            this.items = items;
        }

        public static Product from(AddProductCartForm form) {
            return Product.builder()
                    .id(form.getId())
                    .sellerId(form.getSellerId())
                    .name(form.getName())
                    .description(form.getDescription())
                    .items(form.getItems().stream().map(ProductItem::from).collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProductItem {
        private Long id;
        private String name;
        private Integer count;
        private Integer price;

        @Builder
        public ProductItem(Long id, String name, Integer count, Integer price) {
            this.id = id;
            this.name = name;
            this.count = count;
            this.price = price;
        }

        public static ProductItem from(AddProductCartForm.ProductItem form) {
            return ProductItem.builder()
                    .id(form.getId())
                    .name(form.getName())
                    .count(form.getCount())
                    .price(form.getPrice())
                    .build();
        }

        public void modifyCount(Integer count) {
            this.count += count;
        }
    }
}
