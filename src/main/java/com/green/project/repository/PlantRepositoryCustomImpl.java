package com.green.project.repository;

import com.green.project.dto.DictionaryDto;
import com.green.project.dto.QDictionaryDto;
import com.green.project.dto.SearchDto;
import com.green.project.entity.QPlant;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class PlantRepositoryCustomImpl implements PlantRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public PlantRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {

        if (StringUtils.equals("korean", searchBy)) {
            return QPlant.plant.plantName.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("english", searchBy)) {
            return QPlant.plant.plantName1.like("%" + searchQuery + "%");
        }
        return null;
    }


    @Override
    public Page<DictionaryDto> getPlantPage(SearchDto searchDto, DictionaryDto dictionaryDto, Pageable pageable) {
        QPlant plant = QPlant.plant;

        List<DictionaryDto> content = queryFactory
                .select(new QDictionaryDto(
                        plant.id,
                        plant.imgSrc,
                        plant.plantName,
                        plant.plantName1,
                        plant.plantName2,
                        plant.plantName3,
                        plant.origin,
                        plant.advice,
                        plant.height,
                        plant.width,
                        plant.leafShape,
                        plant.breeding,
                        plant.functional
                        )
                )
                .from(plant)
                .where(searchByLike(searchDto.getSearchBy(), searchDto.getSearchQuery()))
                .orderBy(plant.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count)
                .from(plant)
                .where(searchByLike(searchDto.getSearchBy(), searchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}

