package com.zerobase.cms.order.domain.product;

import com.zerobase.cms.order.domain.model.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private List<ProductItemDto> items;

    @Builder
    public ProductDto(Long id, String name, String description, List<ProductItemDto> items) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.items = items;
    }

    public static ProductDto from(Product product) {
        List<ProductItemDto> items = product.getProductItems().stream()
                .map(ProductItemDto::from).collect(Collectors.toList());

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .items(items)
                .build();
    }

    public static ProductDto withOustItemsFrom(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .build();
    }
}
