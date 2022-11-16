package com.green.project.repository;

import com.green.project.constant.ItemSellStatus;
import com.green.project.dto.SearchDto;
import com.green.project.entity.Item;
import com.green.project.entity.QItem;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {
    private JPAQueryFactory queryFactory; // 동적으로 쿼리를 생성하기 위해 사용

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDateAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("오늘", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("일주일", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1개월", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("3개월", searchDateType)) {
            dateTime = dateTime.minusMonths(3);
        } else if (StringUtils.equals("6개월", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {

        if (StringUtils.equals("itemName", searchBy)) {
            return QItem.item.itemName.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("itemCategory", searchBy)) {
            return QItem.item.itemCategory.stringValue().matches("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Item> getItemManagementPage(SearchDto searchDto, Pageable pageable, String brand) {
        List<Item> content = queryFactory
                .selectFrom(QItem.item)
                .where(QItem.item.itemBrand.eq(brand),
                        regDateAfter(searchDto.getSearchDateType()),
                        searchSellStatusEq(searchDto.getSearchSellStatus()),
                        searchByLike(searchDto.getSearchBy(),
                                searchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(QItem.item.itemBrand.eq(brand),
                        regDateAfter(searchDto.getSearchDateType()),
                        searchSellStatusEq(searchDto.getSearchSellStatus()),
                        searchByLike(searchDto.getSearchBy(), searchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
