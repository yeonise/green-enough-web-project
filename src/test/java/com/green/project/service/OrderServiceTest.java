package com.green.project.service;

import com.green.project.constant.ItemCategory;
import com.green.project.constant.ItemSellStatus;
import com.green.project.constant.OrderStatus;
import com.green.project.dto.OrderDto;
import com.green.project.entity.Item;
import com.green.project.entity.Member;
import com.green.project.entity.Order;
import com.green.project.entity.OrderItem;
import com.green.project.repository.ItemRepository;
import com.green.project.repository.MemberRepository;
import com.green.project.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class OrderServiceTest {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    public Item saveItem() {
        Item item = Item.builder()
                .itemName("에어포스")
                .itemBrand("나이키")
                .itemCategory(ItemCategory.TOOL)
                .itemSellStatus(ItemSellStatus.SELL)
                .price(100000)
                .stock(100)
                .build();
        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = Member.builder()
                .email("test@email.com")
                .build();
        return memberRepository.save(member);
    }

//    @Test
//    @DisplayName("상품 주문 테스트")
    public void orderTest() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(item.getId());
        orderDto.setCount(10);

        Long orderId = orderService.order(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItemList = order.getOrderItems();

        int totalPrice = orderDto.getCount() * item.getPrice();

        assertEquals(totalPrice, order.getTotalPrice());

    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrderTest() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(item.getId());
        orderDto.setCount(10);

        Long orderId = orderService.order(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        orderService.cancelOrder(orderId);

        assertEquals(OrderStatus.CANCEL, order.getOrderStatus());
        assertEquals(100, item.getStock());
    }
}
