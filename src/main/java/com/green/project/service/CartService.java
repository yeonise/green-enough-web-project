package com.green.project.service;

import com.green.project.dto.CartItemDto;
import com.green.project.dto.CartOrderDto;
import com.green.project.dto.MyPageCartDto;
import com.green.project.dto.OrderDto;
import com.green.project.entity.Cart;
import com.green.project.entity.CartItem;
import com.green.project.entity.Item;
import com.green.project.entity.Member;
import com.green.project.repository.CartItemRepository;
import com.green.project.repository.CartRepository;
import com.green.project.repository.ItemRepository;
import com.green.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    public Long addCart(CartItemDto cartItemDto, String email){

        Item item = itemRepository.findById(cartItemDto.getItemId()) // 장바구니에 담을 상품 엔티티를 조회
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email); // 현재 로그인한 회원 엔티티를 조회

        Cart cart = cartRepository.findByMemberId(member.getId()); // 현재 로그인한 회원의 장바구니 엔티티를 조회
        if (cart == null) { // 기존 장바구니가 없을 경우 새로 생성
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId()); // 현재 상품이 장바구니에 이미 들어가 있는지 조회

        if (savedCartItem != null) { // 이미 들어가 있던 상품일 경우 기존 수량에 추가로 담을 수량을 더하기
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else { // 새로 담는 상품인 경우 새로 CartItem 엔티티를 생성
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem); // 장바구니에 들어갈 상품 저장
            return cartItem.getId();
        }
    }

    @Transactional(readOnly = true)
    public List<MyPageCartDto> getMyPageCartList(String email) {

        List<MyPageCartDto> myPageCartDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId()); // 현재 로그인한 회원의 장바구니 조회
        if (cart == null) { // 장바구니에 상품을 하나도 담지 않았을 경우 엔티티가 없으므로 빈 리스트를 반환
            return myPageCartDtoList;
        }

        myPageCartDtoList = cartItemRepository.findMyPageCartDtoList(cart.getId()); // 장바구니에 담겨있는 상품 정보 조회
        return myPageCartDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {
        Member currentMember = memberRepository.findByEmail(email); // 현재 로그인한 회원 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember(); // 장바구니에 상품을 저장한 회원 조회

        if(!StringUtils.equals(currentMember.getEmail(), savedMember.getEmail())) { // 현재 로그인한 회원과 장바구니에 상품을 저장한 회원이 동일한지 검사
            return false;
        }
        return true;
    }

    public void updateCartItemCount(Long cartItemId, int count) { // 장바구니 상품의 수량을 업데이트
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }

    public void deleteCartItem(Long cartItemId) { // 장바구니안의 상품 번호를 파라미터로 받아서 삭제하는 로직
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {
        List<OrderDto> orderDtoList = new ArrayList<>();

        for (CartOrderDto cartOrderDto : cartOrderDtoList) { // 장바구니 페이지에서 전달받은 상품 번호를 이용하여 orderDto 객체 생성
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.orders(orderDtoList, email);

        for (CartOrderDto cartOrderDto : cartOrderDtoList) { // 주문한 상품들을 장바구니에서 삭제
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

        return orderId;
    }

}
