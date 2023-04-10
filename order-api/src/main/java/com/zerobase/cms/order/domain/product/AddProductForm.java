package com.zerobase.cms.order.domain.product;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddProductForm {

    private String name;
    private String description;
    private List<AddProductItemForm> items;

    @Builder
    public AddProductForm(String name, String description, List<AddProductItemForm> items) {
        this.name = name;
        this.description = description;
        this.items = items;
    }
}
