package com.boot.shopping.repository;

import com.boot.shopping.dto.ItemSearchDto;
import com.boot.shopping.dto.MainItemDto;
import com.boot.shopping.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
