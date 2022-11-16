package com.green.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@ToString
public class CartItem extends BaseEntity {

    @Id
    @Column(name = "cart_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart); // 장바구니
        cartItem.setItem(item); // 담은 상품
        cartItem.setCount(count); // 상품 수량
        return cartItem;
    }

    public void addCount(int count) { // 이미 장바구니에 담겨 있는 상품을 추가로 담을 경우
        this.count += count;
    }

    public void updateCount(int count) { // 장바구니에 담겨있는 상품 수량 변경 메소드
        this.count = count;
    }

}
