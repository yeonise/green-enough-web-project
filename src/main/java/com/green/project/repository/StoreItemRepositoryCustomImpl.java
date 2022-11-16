package com.green.project.repository;

import com.green.project.dto.SearchDto;
import com.green.project.dto.QStoreDto;
import com.green.project.dto.StoreDto;
import com.green.project.entity.QItem;
import com.green.project.entity.QItemImg;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class StoreItemRepositoryCustomImpl implements StoreItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public StoreItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {

        if (StringUtils.equals("itemName", searchBy)) {
            return QItem.item.itemName.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("itemBrand", searchBy)) {
            return QItem.item.itemBrand.like("%" + searchQuery + "%");
        }
        return null;
    }

    @Override
    public Page<StoreDto> getItemsByItemCategory(SearchDto searchDto, StoreDto storeDto, Pageable pageable, String itemCategory) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<StoreDto> content = queryFactory
                .select(new QStoreDto(
                        item.id,
                        item.itemName,
                        item.itemBrand,
                        itemImg.imgUrl,
                        item.price,
                        item.itemSellStatus)
                )
                .from(itemImg)
                .where(item.itemCategory.stringValue().eq(itemCategory),
                        searchByLike(searchDto.getSearchBy(), searchDto.getSearchQuery()))
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count)
                .from(itemImg)
                .where(item.itemCategory.stringValue().eq(itemCategory),
                        searchByLike(searchDto.getSearchBy(), searchDto.getSearchQuery()))
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

}
