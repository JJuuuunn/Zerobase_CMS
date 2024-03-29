package com.zerobase.cms.order.application;

import com.zerobase.cms.order.client.UserClient;
import com.zerobase.cms.order.client.user.ChangeBalanceForm;
import com.zerobase.cms.order.client.user.CustomerDto;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.exception.ErrorCode;
import com.zerobase.cms.order.service.ProductItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class OrderApplication {

    // 결제를 위해 필요한 것
    // 1. 물건들이 전부 주문 가능한 상태인지 확인
    // 2. 가격 변동이 있었는지 확인
    // 3. 고객의 돈이 충분한지 확인
    // 4. 결제 & 상품의 재고 관리

    private final CartApplication cartApplication;
    private final UserClient userClient;
    private final ProductItemService productItemService;

    @Transactional
    public void order(String token, Cart cart) {
        // 1. 주문시 기존 카트 버림
        // 2. 선택 주문시 선택하지 않은 상품 살리기 - 숙제
        Cart orderCart = cartApplication.refreshCart(cart);
        if (orderCart.getMessages().size() > 0) {
            // 문제시
            throw new CustomException(ErrorCode.ORDER_FAIL_CHECK_CART);
        }
        CustomerDto customerDto = userClient.getCustomerInfo(token).getBody();

        int totalPrice = getTotalPrice(cart);
        if (customerDto.getBalance() < totalPrice) {
            throw new CustomException(ErrorCode.ORDER_FAIL_NO_MONEY);
        }
        // 롤백 계획에 대해 생각해야함
        userClient.changeBalance(token, ChangeBalanceForm.builder()
                .from("USER")
                .message("ORDER")
                .money(-totalPrice)
                .build()
        );
        for (Cart.Product product : orderCart.getProducts()) {
            for (Cart.ProductItem cartItem : product.getItems()) {
                ProductItem productItem = productItemService.getProductItem(cartItem.getId());
                productItem.changeCount(productItem.getCount() - cartItem.getCount());
            }
        }
    }

    public Integer getTotalPrice(Cart cart) {

        return cart.getProducts().stream()
                .flatMapToInt(product -> product.getItems().stream()
                        .flatMapToInt(
                                productItem -> IntStream.of(productItem.getPrice() * productItem.getCount())))
                .sum();
    }

}
