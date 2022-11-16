package com.green.project.dto;

import com.green.project.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private String itemName; // 상품명
    private String itemBrand; // 상품의 브랜드명
    private int count; // 주문 수량
    private int orderPrice; // 주문 금액
    private String imgUrl; // 상품 이미지 경로

    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemName = orderItem.getItem().getItemName();
        this.itemBrand = orderItem.getItem().getItemBrand();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
}
