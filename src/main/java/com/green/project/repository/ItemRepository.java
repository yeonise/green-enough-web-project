package com.green.project.repository;

import com.green.project.constant.ItemCategory;
import com.green.project.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom, StoreItemRepositoryCustom {

    boolean existsByItemBrand(String itemBrand);

    List<Item> findByItemName(String itemName);

    List<Item> findByItemNameOrItemBrand(String itemName, String itemBrand);

    List<Item> findByItemCategoryOrderByPriceDesc(ItemCategory itemCategory);

    @Query("SELECT i FROM Item i WHERE i.itemBrand LIKE %:itemBrand% ORDER BY i.price DESC")
    List<Item> findByItemBrandOrderByPriceDesc(@Param("itemBrand") String itemBrand);
}

// @Query 어노테이션을 사용하면 JPQL 객체지향 쿼리 언어를 통해 복잡한 쿼리도 처리 가능
// SQL 문법과 유사
// SQL 은 데이터베이스의 테이블을 대상으로 쿼리를 수행하고 JPQL 은 엔티티 객체를 대상으로 쿼리를 수행한다.
// JPQL 은 SQL 을 추상화해서 사용하기 때문에 특정 데이터베이스 SQL 에 의존하지 않는다.
// 기존 데이터베이스에서 사용하던 쿼리를 그대로 사용해야 할 때는 @Query 의 nativeQuery 속성을 사용하면 기존 쿼리를 그대로 활용할 수 있다.
// 단, 데이터베이스에 대해 독립적이라는 장점을 잃어버린다.
// JPQL 은 문자열을 입력하기 때문에 잘못 입력하면 컴파일 시점에 에러를 발견할 수 없다는 단점이 있다.
// 이를 보완할 수 있는 방법으로 Querydsl 을 사용한다.
// Querydsl : JPQL 을 코드로 작성할 수 있도록 도와주는 빌더 API
// 동적으로 쿼리를 생성할 수 있다는 것이 큰 장점이다.
