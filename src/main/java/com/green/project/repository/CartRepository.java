package com.green.project.repository;


import com.green.project.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(Long memberId); // 현재 로그인한 회원의 엔티티를 찾기 위한 쿼리

}
