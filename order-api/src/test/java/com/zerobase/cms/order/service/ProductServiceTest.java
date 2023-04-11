package com.zerobase.cms.order.service;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.repository.ProductItemRepository;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;

    @Test
    public void ADD_PRODUCT_TEST(){
        //given
        Long sellerId = 1L;
        AddProductForm form = makeProductForm("나이키 에어포스", "나이키 신발입니다.", 3);

        Product product = productService.addProduct(sellerId, form);

        //when
        Product findProduct = productRepository.findWithProductItemById(product.getId()).get();

        //then
        assertNotNull(findProduct);

        // 나머지 필드들에 대한 검증
        assertEquals(findProduct.getName(), "나이키 에어포스");
        assertEquals(findProduct.getDescription(), "나이키 신발입니다.");
        assertEquals(findProduct.getProductItems().size(), 3);
        for (int i = 0; i < findProduct.getProductItems().size(); i++) {
            assertEquals(findProduct.getProductItems().get(i).getName(), "나이키 에어포스" + i);
            assertEquals(findProduct.getProductItems().get(i).getPrice(), 10000);
            assertEquals(findProduct.getProductItems().get(i).getCount(), 1);
        }
    }

    private static final AddProductForm makeProductForm(String name, String description, int itemCount) {
        List<AddProductItemForm> itemForms = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            itemForms.add(makeProductItemForm(null, name + i));
        }
        return AddProductForm.builder()
                .name(name)
                .description(description)
                .items(itemForms)
                .build();
    }

    private static final AddProductItemForm makeProductItemForm(Long productId, String name) {
        return AddProductItemForm.builder()
                .productId(productId)
                .name(name)
                .price(10000)
                .count(1)
                .build();
    }
}