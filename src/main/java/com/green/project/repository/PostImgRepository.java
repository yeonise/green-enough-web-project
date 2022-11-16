package com.green.project.repository;

import com.green.project.entity.PostImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImgRepository extends JpaRepository<PostImg, Long> {

    List<PostImg> findByPostIdOrderByIdAsc(Long postId);

    PostImg findByPostIdAndRepImgYn(Long itemId, String repImgYn); // 게시글의 대표 이미지를 찾는 쿼리

}
