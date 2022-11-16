package com.green.project.entity;

import com.green.project.constant.ItemCategory;
import com.green.project.constant.ItemSellStatus;
import com.green.project.dto.ItemFormDto;
import com.green.project.exception.OutOfStockException;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item")
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemName;

    @Column(nullable = false, length = 30)
    private String itemBrand;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemImg> itemImgList = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemDetailImg> itemDetailImgList = new ArrayList<>();

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemName = itemFormDto.getItemName();
        this.itemBrand = itemFormDto.getItemBrand();
        this.itemCategory = itemFormDto.getItemCategory();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
        this.price = itemFormDto.getPrice();
        this.stock = itemFormDto.getStock();
    }

    public void removeStock(int removedStock) {
        int restStock = this.stock - removedStock;
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다. 현재 상품이 " + this.stock + "개 남아있습니다.");
        }
        this.stock = restStock;
    }

    public void addStock(int stock) { // 주문을 취소할 경우 주문 수량만큼 다시 상품의 재고를 증가시키는 메소드
        this.stock += stock;
    }

}
