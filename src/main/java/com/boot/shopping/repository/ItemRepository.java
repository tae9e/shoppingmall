package com.boot.shopping.repository;
//(1)Query Method
//(2)@Query()
//(3)QueryDSL - 에러를 빨리 찾기 위함(복잡)
//              JPQL을 코드로 작성할 수 있도록 도와주는 builder
//              고정된 SQL문이 아닌 조건에 맞게 동적으로 Query 생성
//              의존성 추가
import com.boot.shopping.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

//test로 이동: Ctrl+Shift+t
public interface ItemRepository extends JpaRepository<Item,Long>,
        QuerydslPredicateExecutor<Item>,
        ItemRepositoryCustom {
    //QuerydslPredicateExecutor 인터페이스 메소드
        //predicate: 조건에 맞는
        //long count(predicate); 조건에 맞는 데이터 총 개수 반환
        //boolean exists(predicate); 조건에 맞는 데이터 존재 여부 반환
        //Page<T>findAll(predicate,pageable): 조건에 맞는 페이지 데이터 반환




    //      1.상품 이름을 이용해서 데이터 조회
    //    findByItemNum(String itemNum)이 메소드를 수행하면 값이 List가 나옴
        List<Item> findByItemNm(String itemNum);

        //2.상품명과 상품상세 설명을 OR조건을 이용해서 조회
        List<Item> findByItemNmOrItemDetail(String ItemNm,String ItemDetail);

        //3.파라미터로 넘어온 price 변수보다 값이 작은 상품 조회
        List<Item> findByPriceLessThan(Integer price);

        //4.상품의 가격이 높은 순으로 조회
        List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

        //5.Query()
        @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
        List<Item> findByItemDetail(@Param("itemDetail")String itemDetail);

        //6.@Query()-nativeQuery(기존 DB SQL)
        @Query(value = "select * from Item i where i.item_Detail like %:itemDetail% order by i.price desc",nativeQuery = true)
        List<Item> findByItemDetailByNative(@Param("itemDetail")String itemDetail);


}
