package com.green.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageCartDto {
    private Long cartItemId; // 장바구니 안의 상품 아이디
    private String itemName; // 상품명
    private String itemBrand; // 상품의 브랜드명
    private int price; // 상품 금액
    private int count; // 수량
    private String imgUrl; // 상품 이미지 경로
    private Long itemId; // 상품의 아이디

    public MyPageCartDto(Long cartItemId, String itemName, String itemBrand, int price, int count, String imgUrl, Long itemId) {
        this.cartItemId = cartItemId;
        this.itemName = itemName;
        this.itemBrand = itemBrand;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
        this.itemId = itemId;
    }

}
