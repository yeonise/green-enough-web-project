package com.green.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem extends BaseTimeEntity {

    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item); // 주문할 상품
        orderItem.setCount(count); // 주문할 수량
        orderItem.setOrderPrice(item.getPrice()); // 현재 시간 기준으로 상품 가격을 주문 가격으로 세팅

        item.removeStock(count); // 주문할 수량만큼 재고에서 빼기
        return orderItem;
    }

    public int getTotalPrice() {
        return orderPrice * count;
    } // 주문한 총 가격을 계산하는 메소드

    public void cancel() { // 주문 취소 시 주문 수량만큼 상품의 재고 증가
        this.getItem().addStock(count);
    }
}
