package com.green.project.repository;

import com.green.project.dto.DictionaryDto;
import com.green.project.dto.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlantRepositoryCustom {

    Page<DictionaryDto> getPlantPage(SearchDto searchDto, DictionaryDto dictionaryDto, Pageable pageable);

}
