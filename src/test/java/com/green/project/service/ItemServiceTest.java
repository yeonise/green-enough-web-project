package com.green.project.service;

import com.green.project.constant.ItemCategory;
import com.green.project.constant.ItemSellStatus;
import com.green.project.dto.ItemFormDto;
import com.green.project.entity.Item;
import com.green.project.entity.ItemDetailImg;
import com.green.project.entity.ItemImg;
import com.green.project.repository.ItemDetailImgRepository;
import com.green.project.repository.ItemImgRepository;
import com.green.project.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    @Autowired
    ItemDetailImgRepository itemDetailImgRepository;

    @Autowired
    ItemService itemService;

    List<MultipartFile> createMultipartFiles() throws Exception {

        List<MultipartFile> multipartFileList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String path = "/Users/yeon/Documents/project/item";
            String imageName = "image" + i +".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
            multipartFileList.add(multipartFile);
        }
        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "business", roles = "BUSINESS")
    void saveItem() throws Exception {
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemBrand("테스트 브랜드명");
        itemFormDto.setItemName("테스트 상품명");
        itemFormDto.setItemCategory(ItemCategory.POT);
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setPrice(20000);
        itemFormDto.setStock(100);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList, multipartFileList);

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemDetailImg> itemDetailImgList = itemDetailImgRepository.findByItemIdOrderByIdAsc(itemId);

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        assertEquals(itemFormDto.getItemName(), item.getItemName());
        assertEquals(itemFormDto.getItemBrand(), item.getItemBrand());
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(itemFormDto.getItemCategory(), item.getItemCategory());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getStock(), item.getStock());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemDetailImgList.get(0).getOriImgName());
    }
}
