package com.green.project.repository;


import com.green.project.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Post findByMemberId(Long memberId); // 현재 로그인한 회원의 엔티티를 찾기 위한 쿼리

    @Modifying
    @Query("UPDATE Post p SET p.hits = p.hits + 1 WHERE p.id = :id")
    int updateHits(@Param("id") Long id);

    boolean existsByWriter(String writer);

    List<Post> findByWriter(String writer);

}
