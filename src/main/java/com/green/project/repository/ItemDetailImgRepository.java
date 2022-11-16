package com.green.project.repository;

import com.green.project.entity.ItemDetailImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemDetailImgRepository extends JpaRepository<ItemDetailImg, Long> {

    List<ItemDetailImg> findByItemIdOrderByIdAsc(Long itemId);

}
