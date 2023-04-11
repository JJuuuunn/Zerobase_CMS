package com.zerobase.cms.order.domain.product;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateProductForm {

    private Long id;
    private String name;
    private String description;
    private List<UpdateProductItemForm> items;

    @Builder
    public UpdateProductForm(Long id, String name, String description, List<UpdateProductItemForm> items) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.items = items;
    }
}
