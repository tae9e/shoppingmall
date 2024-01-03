package com.boot.shopping.repository;

import com.boot.shopping.constant.ItemSellStatus;
import com.boot.shopping.entity.Item;

import com.boot.shopping.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static com.boot.shopping.entity.QItem.item;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest(){
       for(int i =1; i<=10; i++){
           Item item = new Item();
           item.setItemNm("테스트 상품"+i);
           item.setPrice(10000+i);
           item.setItemDetail("테스트 상품 상세설명"+i);
           item.setItemSellStatus(ItemSellStatus.SELL);
           item.setStockNumber(100);
           item.setRegTime(LocalDateTime.now());
           item.setUpdateTime(LocalDateTime.now());
           Item savedItem=itemRepository.save(item);
           System.out.println(savedItem.toString());
       }
    }

    //      상품 이름을 이용해서 데이터 조회-select
    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNumTest(){
        this.createItemTest();
        List<Item> list = itemRepository.findByItemNm("테스트 상품1");
        for(Item item:list){
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("상품명 or 상품 상세설명 조회 테스트")
    public void findByItemNmOrItemDetail() {
        this.createItemTest();
        List<Item> list = itemRepository.findByItemNmOrItemDetail("테스트 상품1","테스트 상품 상세설명5");
        for(Item item:list){
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("특정 가격보다 작은 상품 조회")
    public void findByPriceLessThan() {
        this.createItemTest();
        List<Item> list = itemRepository.findByPriceLessThan(10004);
        for (Item item : list) {
            System.out.println(item.toString());
        }

    }

    @Test
    @DisplayName("가격이 높은 순으로 조회")
    public void findByPriceLessThanOrderByPriceDescTest() {
        this.createItemTest();
        List<Item> list = itemRepository.findByPriceLessThanOrderByPriceDesc(10007);
        for (Item item : list) {
            System.out.println(item.toString());
        }

    }

    //5.@Query
    @Test
    @DisplayName("@query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest(){
        this.createItemTest();
        List<Item> list = itemRepository.findByItemDetail("테스트 상품 상세설명");
        for (Item item : list) {
            System.out.println(item.toString());
        }

    }

    //6.@Query()-nativeQuery(기존 DB SQL)
    @Test
    @DisplayName("@Query()-nativeQuery를 이용한 상품 조회 테스트")
    public void findByItemDetailByNativeTest(){
        this.createItemTest();
        List<Item> list = itemRepository.findByItemDetailByNative("테스트 상품 상세설명");
        for (Item item : list) {
            System.out.println(item.toString());
        }

    }

    //7.QueryDSL
    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDSLTest(){
        this.createItemTest();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%"+"테스트 상품 상세설명"+"%"))
                .orderBy(qItem.price.desc());
        //fetch() - 조회 결과 리스트 반환
        List<Item> list = query.fetch();
        for (Item item : list) {
            System.out.println(item.toString());
        }


    }

    //8. QuerydslPredicateExcutor를 이용한 상품 조회
    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest2(){
        for(int i=1; i<=5; i++){ //판매중
            Item item = new Item();
            item.setItemNm("테스트 상품"+i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세설명"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);

        }
        for(int i=6; i<=10; i++){ //품절
            Item item = new Item();
            item.setItemNm("테스트 상품"+i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세설명"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
           itemRepository.save(item);

        }
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트2")
    public void queryDslTest2(){
        this.createItemTest2();
        //BooleanBuilder: 쿼리에 들어갈 조건을 만들어주는 builder
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem qItem = item;
        String itemDetail = "테스트 상품 상세설명";
        int price = 10003;
        String itemSellStatus="SELL";
        booleanBuilder.and(item.itemDetail.like("%"+itemDetail+"%"));
        booleanBuilder.and(item.price.gt(price));
        if(StringUtils.equals(itemSellStatus,ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }
        //데이터를 페이징해 조회해서 Pageable 객체 생성
        Pageable pageable = PageRequest.of(0,5);
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder,pageable);
        System.out.println("total elements: " + itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem:resultItemList)
            System.out.println(resultItem.toString());
    }
}

