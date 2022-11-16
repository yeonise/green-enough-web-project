package com.green.project.dto;

import com.green.project.constant.ItemSellStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreDto {

    private Long id;
    private String itemName;
    private String itemBrand;
    private String imgUrl;
    private Integer price;
    private ItemSellStatus itemSellStatus;

    @QueryProjection
    public StoreDto(Long id, String itemName, String itemBrand, String imgUrl, Integer price, ItemSellStatus itemSellStatus) {
        this.id = id;
        this.itemName = itemName;
        this.itemBrand = itemBrand;
        this.imgUrl = imgUrl;
        this.price = price;
        this.itemSellStatus = itemSellStatus;
    }
}
