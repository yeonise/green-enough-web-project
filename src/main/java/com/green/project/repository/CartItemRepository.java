package com.green.project.repository;

import com.green.project.dto.MyPageCartDto;
import com.green.project.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId(Long carId, Long itemId); // 해당 상품이 해당 장바구니에 들어있는지 조회하는 쿼리

    @Query("SELECT new com.green.project.dto.MyPageCartDto(ci.id, i.itemName, i.itemBrand, i.price, ci.count, im.imgUrl, ci.item.id) " +
            "FROM CartItem ci, ItemImg im " +
            "JOIN ci.item i " +
            "WHERE ci.cart.id = :cartId " +
            "AND im.item.id = ci.item.id " +
            "AND im.repImgYn = 'Y' " +
            "ORDER BY ci.regTime DESC")
    List<MyPageCartDto> findMyPageCartDtoList(@Param("cartId") Long cartId);
}
