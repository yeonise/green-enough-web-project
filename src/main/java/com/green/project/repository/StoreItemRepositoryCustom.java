package com.green.project.repository;

import com.green.project.dto.SearchDto;
import com.green.project.dto.StoreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreItemRepositoryCustom {

    Page<StoreDto> getItemsByItemCategory(SearchDto searchDto, StoreDto storeDto, Pageable pageable, String itemCategory);

}
