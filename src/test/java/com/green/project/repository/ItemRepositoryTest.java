package com.green.project.repository;

import com.green.project.constant.ItemCategory;
import com.green.project.constant.ItemSellStatus;
import com.green.project.entity.Item;
import com.green.project.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager entityManager;

//    @Test
//    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        Item item = Item.builder()
                .itemCategory(ItemCategory.PLANT)
                .itemName("상품 저장 테스트 식물 이름")
                .itemBrand("상품 저장 테스트 브랜드 이름")
                .itemSellStatus(ItemSellStatus.SELL)
                .stock(100)
                .price(10000)
                .build();

        Item savedItem = itemRepository.save(item);

        System.out.println(savedItem);

    }

    public void createItemList() {
        for (int i = 1; i <= 5; i++) {
            Item item = Item.builder()
                    .itemCategory(ItemCategory.PLANT)
                    .itemName("테스트 식물 이름 " + i)
                    .itemBrand("아디다스")
                    .itemSellStatus(ItemSellStatus.SOLD_OUT)
                    .stock(10 + i)
                    .price(10000 + i)
                    .build();

            itemRepository.save(item);
        }
    }

//    @Test
//    @DisplayName("상품명 조회 테스트")
    public void findByItemNameTest() {
//        this.createItemList();
        List<Item> itemList = itemRepository.findByItemName("테스트 식물 이름 1");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

//    @Test
//    @DisplayName("상품명 또는 브랜드명 조회 테스트")
    public void findByItemNameOrItemBrandTest() {
        List<Item> itemList = itemRepository.findByItemNameOrItemBrand("테스트 식물 이름 1", "테스트 브랜드 이름");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

//    @Test
//    @DisplayName("상품 카테고리 분류 및 높은 가격 순 정렬 테스트")
    public void findByItemCategoryOrderByPriceDescTest() {
        List<Item> itemList = itemRepository.findByItemCategoryOrderByPriceDesc(ItemCategory.PLANT);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

//    @Test
//    @DisplayName("@Query JPQL 활용 상품 조회 테스트")
    public void findByItemBrandOrderByPriceDescTest() {
//        this.createItemList();
        List<Item> itemList = itemRepository.findByItemBrandOrderByPriceDesc("아디다스");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

//    @Test
//    @DisplayName("Querydsl 조회 테스트 1")
    public void queryDslTest1() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemBrand.like("%" + "아" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch(); // fetch() 메소드 실행 시점에 쿼리문이 실행

        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트 2")
    public void queryDslTest2() {
//        this.createItemList();
        BooleanBuilder booleanBuilder = new BooleanBuilder(); // 쿼리에 들어갈 조건을 만들어주는 빌더
        QItem qItem = QItem.item;
        String itemBrand = "아디다스";
        int price = 10002;
        String itemSellStatus = "SELL";

        booleanBuilder.and(qItem.itemBrand.like("%" + itemBrand + "%"));
        booleanBuilder.and(qItem.price.gt(price));

        if (StringUtils.equals(itemSellStatus, ItemSellStatus.SELL)) {
            booleanBuilder.and(qItem.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(1, 5);
        Page<Item> itemPageResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPageResult.getTotalElements());

        List<Item> resultItemList = itemPageResult.getContent();
        for (Item resultItem : resultItemList) {
            System.out.println(resultItem.toString());
        }

    }

}

// @PersistenceContext : EntityManager 를 Bean 으로 주입받을 때 사용한다.
// 스프링에서는 영속성 관리를 위해 EntityManager 가 존재한다.
// 처음 스프링 컨테이너가 시작될 때 EntityManager 를 만들어서 Bean 으로 등록한다.
// 이때 스프링이 만들었던 EntityManager 를 주입받을 때 사용한다.
// EntityManager 를 사용할 때 여러 쓰레드가 동시에 접근하면 동시성 문제가 발생하기 때문에 쓰레드 간에는 EntityManager 를 공유해서는 안된다.
// 일반적으로 스프링은 싱글톤 기반으로 동작하기 때문에 Bean 을 모든 쓰레드가 공유한다.
// 그러나 @PersistenceContext 로 EntityManager 를 주입받으면 동시성 문제가 발생하지 않는다.
// 동시성 문제가 발생하지 않는 이유는 스프링 컨테이너가 초기화되면서 @PersistenceContext 로 주입받은 EntityManager 를 Proxy 로 감싼다.
// 그리고 EntityManager 호출할 때마다 Proxy 를 통해 EntityManager 를 생성하여 Thread-Safe 를 보장한다.
