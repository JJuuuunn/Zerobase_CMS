package com.zerobase.cms.order.application;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.order.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest//(classes = TestRedisConfig.class)
class CartApplicationTest {

    @Autowired CartApplication cartApplication;
    @Autowired ProductService productService;
    @Autowired ProductRepository productRepository;

    @Test
    public void ADD_TEST() {
        Long customerId = 100L;
        cartApplication.clearCart(customerId);

        //given
        Product p = ADD_PRODUCT();
        //when
        Product findProduct = productRepository.findWithProductItemById(p.getId()).get();

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


        makeAddForm(findProduct);

        Cart cart = cartApplication.addCart(customerId, makeAddForm(findProduct));

        // 데이터가 잘 들어 갔는지 체크
        assertEquals(cart.getMessages().size(), 0);

        cart = cartApplication.getCart(customerId);
        assertEquals(cart.getMessages().size(), 1);


//        p.getProductItems().get(0).changeCount(0);
//        productRepository.save(p);
//        cartApplication.getCart();
    }

    AddProductCartForm makeAddForm(Product p) {
        AddProductCartForm.ProductItem productItem =
                AddProductCartForm.ProductItem.builder()
                        .id(p.getProductItems().get(0).getId())
                        .name(p.getProductItems().get(0).getName())
                        .count(5)
                        .price(20000)
                        .build();

        return AddProductCartForm.builder()
                        .id(p.getId())
                        .sellerId(p.getSellerId())
                        .name(p.getName())
                        .description(p.getDescription())
                        .items(List.of(productItem))
                        .build();
    }

    Product ADD_PRODUCT(){
        Long sellerId = 1L;
        AddProductForm form = makeProductForm("나이키 에어포스", "나이키 신발입니다.", 3);

        return productService.addProduct(sellerId, form);
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