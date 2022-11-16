package com.green.project.repository;

import com.green.project.dto.SearchDto;
import com.green.project.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getItemManagementPage(SearchDto searchDto, Pageable pageable, String brand);
}
