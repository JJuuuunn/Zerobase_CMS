package com.zerobase.cms.order.application;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.exception.ErrorCode;
import com.zerobase.cms.order.service.CartService;
import com.zerobase.cms.order.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartApplication {

    private final ProductSearchService productSearchService;
    private final CartService cartService;

    public Cart addCart(Long customerId, AddProductCartForm form) {
        Product product = productSearchService.getByProductId(form.getId());
        if (product == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_PRODUCT);
        }
        Cart cart = cartService.getCart(customerId);

        if (cart != null && !addAble(cart, product, form)) {
            throw new CustomException(ErrorCode.ITEM_COUNT_NOT_ENOUGH);
        }

        return cartService.addCart(customerId, form);
    }

    private boolean addAble(Cart cart, Product product, AddProductCartForm form) {
        Cart.Product cartProduct = cart.getProducts().stream().filter(p -> p.getId().equals(form.getId()))
                .findFirst().orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
        Map<Long, Integer> cartItemCountMap = cartProduct.getItems().stream()
                .collect(Collectors.toMap(Cart.ProductItem::getId, Cart.ProductItem::getCount));
        Map<Long, Integer> currentItemCountMap = product.getProductItems().stream()
                .collect(Collectors.toMap(ProductItem::getId, ProductItem::getCount));

        return form.getItems().stream().noneMatch(
                formItem -> {
                    Integer cartCount = cartItemCountMap.get(formItem.getId());
                    if (cartCount == null) {
                        cartCount = 0;
                    }

                    Integer currentCount = currentItemCountMap.get(formItem.getId());
                    if (currentCount == null) {
                        currentCount = 0;
                    }
                    return formItem.getCount() + cartCount > currentCount;
                });
    }

    // 1. 장바구니에 상품을 추가
    // 2. 상품 가격이나 수량이 변동
    public Cart getCart(Long customerId) {
        // 2. 메세지를 보고 난 다음, 본 메세지는 스팸이 되기 때문에 제거
        Cart cart = refreshCart(cartService.getCart(customerId));
        Cart returnCart = Cart.builder()
                .customerId(customerId)
                .products(cart.getProducts())
                .messages(cart.getMessages())
                .build();
        cart.emptyMessages();
        // 메세지 없는 것
        cartService.putCart(customerId, cart);
        return returnCart;
    }

    private Cart refreshCart(Cart cart) {
        // 1. 상품이나 상품의 아템의 정보, 가격, 수량이 변경되었는지 체크 후 알림 제공
        // 2. 상품 수량, 가격을 우리가 임의로 변경

        Map<Long, Product> productMap = productSearchService.getListByProductIds(
                        cart.getProducts().stream()
                                .map(Cart.Product::getId).collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        for (int i = 0; i < cart.getProducts().size(); i++) {
            Cart.Product cartProduct = cart.getProducts().get(i);

            Product p = productMap.get(cartProduct.getId());
            if (p == null) {
                cart.getProducts().remove(cartProduct);
                i--;
                cart.addMessage(cartProduct.getName() + "상품이 삭제되었습니다.");
                continue;
            }

            Map<Long, ProductItem> productItemMap = p.getProductItems().stream()
                    .collect(Collectors.toMap(ProductItem::getId, productItem -> productItem));

            List<String> tmpMessage = new ArrayList<>();
            // 각각 케이스 별로에러를 쪼개고, 에러가 정상 출력 되야 하는지 체크
            for (int j = 0; j < cartProduct.getItems().size(); j++) {
                // Cart.ProductItem cartProductItem : cartProduct.getItems()
                Cart.ProductItem cartProductItem = cartProduct.getItems().get(j);
                ProductItem pi = productItemMap.get(cartProductItem.getId());
                if (pi == null) {
                    cartProduct.getItems().remove(cartProductItem);
                    j--;
                    tmpMessage.add(cartProductItem.getName() + "옵션이 삭제되었스빈다.");
                    continue;
                }

                boolean isPriceChanged = false, isCountNotEnough = false;

                if (!cartProductItem.getPrice().equals(pi.getPrice())) {
                    isPriceChanged = true;
                    cartProductItem.changePrice(pi.getPrice());
                }
                if (cartProductItem.getCount() > productItemMap.get(cartProductItem.getId()).getCount()) {
                    isCountNotEnough = true;
                    cartProductItem.changeCount(pi.getCount());
                }
                if (isPriceChanged && isCountNotEnough) {
                    // message 1
                    tmpMessage.add(cartProductItem.getName() + "가격이 변동되고, 수량이 부족하여 구매 가능한 최대치로 변경되었습니다.");
                } else if (isPriceChanged) {
                    // message 2
                    tmpMessage.add(cartProductItem.getName() + "가격이 변동되었습니다.");
                } else if (isCountNotEnough) {
                    // message 3
                    tmpMessage.add(cartProductItem.getName() + "수량이 부족하여 구매 가능한 최대치로 변경되었습니다.");
                }
            }
            //
            if (cartProduct.getItems().size() == 0) {
                cart.getProducts().remove(cartProduct);
                i--;
                cart.addMessage(cartProduct.getName() + " 상품의 옵션이 모두 없어져 구매가 불가능합니다.");
                continue;
            } else if (tmpMessage.size() > 0) {
                StringBuilder builder = new StringBuilder();
                builder.append(cartProduct.getName() + " 상품의 변동 사항 : ");
                for (String message : tmpMessage) {
                    builder.append(message)
                            .append(", ");
                }
                cart.addMessage(builder.toString());
            }
        }
        cartService.putCart(cart.getCustomerId(), cart);
        return cart;
    }
}
