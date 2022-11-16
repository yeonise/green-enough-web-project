package com.green.project.service;

import com.green.project.constant.ItemCategory;
import com.green.project.constant.ItemSellStatus;
import com.green.project.dto.CartItemDto;
import com.green.project.entity.CartItem;
import com.green.project.entity.Item;
import com.green.project.entity.Member;
import com.green.project.repository.CartItemRepository;
import com.green.project.repository.ItemRepository;
import com.green.project.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CartService cartService;

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

    @Test
    @DisplayName("장바구니에 상품 담기 테스트")
    public void addCart() {
        Item item = saveItem();
        Member member = saveMember();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setItemId(item.getId());
        cartItemDto.setCount(5);

        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        assertEquals(item.getId(), cartItem.getItem().getId());
        assertEquals(cartItemDto.getCount(), cartItem.getCount());
    }
}
