package com.green.project.service;

import com.green.project.dto.ItemFormDto;
import com.green.project.dto.SearchDto;
import com.green.project.dto.StoreDto;
import com.green.project.dto.img.ItemDetailImgDto;
import com.green.project.dto.img.ItemImgDto;
import com.green.project.entity.Item;
import com.green.project.entity.ItemDetailImg;
import com.green.project.entity.ItemImg;
import com.green.project.repository.ItemDetailImgRepository;
import com.green.project.repository.ItemImgRepository;
import com.green.project.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemDetailImgRepository itemDetailImgRepository;
    private final ItemImgService itemImgService;
    private final ItemDetailImgService itemDetailImgService;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList, List<MultipartFile> itemDetailImgFileList) throws Exception {
        // 상품 등록하기
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        // 이미지 등록하기
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if (i == 0) // 첫 번째 이미지의 대표 여부 값을 'Y'로 설정하기
                itemImg.setRepImgYn("Y");
            else // 나머지 상품 이미지는 'N'로 설정하기
                itemImg.setRepImgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); // 상품의 이미지 정보를 저장하기
        }

        // 상세 이미지 등록하기
        for (int i = 0; i < itemDetailImgFileList.size(); i++) {
            ItemDetailImg itemDetailImg = new ItemDetailImg();
            itemDetailImg.setItem(item);
            itemDetailImgService.saveItemDetailImg(itemDetailImg, itemDetailImgFileList.get(i)); // 상품의 상세 이미지 정보를 저장하기
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDetail(Long itemId) {
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemDetailImg> itemDetailImgList = itemDetailImgRepository.findByItemIdOrderByIdAsc(itemId);

        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        List<ItemDetailImgDto> itemDetailImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) { // 조회한 ItemImg 엔티티를 ItemImgDto 객체로 만들어서 리스트에 추가하기
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        for (ItemDetailImg itemDetailImg : itemDetailImgList) { // 조회한 ItemDetailImg 엔티티를 ItemDetailImgDto 객체로 만들어서 리스트에 추가하기
            ItemDetailImgDto itemDetailImgDto = ItemDetailImgDto.of(itemDetailImg);
            itemDetailImgDtoList.add(itemDetailImgDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new); // 상품 아이디로 상품 엔티티 조회하기
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        itemFormDto.setItemDetailImgDtoList(itemDetailImgDtoList);

        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList, List<MultipartFile> itemDetailImgFileList) throws Exception {
        // 상품 수정
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImgIds = itemFormDto.getItemImgIds();
        List<Long> itemDetailImgIds = itemFormDto.getItemDetailImgIds();

        // 이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        // 상세 이미지 등록
        for (int i = 0; i < itemDetailImgFileList.size(); i++) {
            itemDetailImgService.updateItemDetailImg(itemDetailImgIds.get(i), itemDetailImgFileList.get(i));
        }

        return item.getId();
    }
    @Transactional
    public void deleteItem(Long itemId) throws Exception {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id = " + itemId));
        itemRepository.delete(item);
    }

    @Transactional(readOnly = true) // 사업자 관리 페이지에 상품 불러오기
    public Page<Item> getItemManagementPage(SearchDto searchDto, Pageable pageable, String brand) {
        return itemRepository.getItemManagementPage(searchDto, pageable, brand);
    }

    @Transactional(readOnly = true)
    public Page<StoreDto> getStorePlantItemPage(SearchDto searchDto, StoreDto storeDto, Pageable pageable) {
        return itemRepository.getItemsByItemCategory(searchDto, storeDto, pageable, "PLANT");
    }

    @Transactional(readOnly = true)
    public Page<StoreDto> getStoreToolItemPage(SearchDto searchDto, StoreDto storeDto, Pageable pageable) {
        return itemRepository.getItemsByItemCategory(searchDto, storeDto, pageable, "TOOL");
    }

    @Transactional(readOnly = true)
    public Page<StoreDto> getStorePotItemPage(SearchDto searchDto, StoreDto storeDto, Pageable pageable) {
        return itemRepository.getItemsByItemCategory(searchDto, storeDto, pageable, "POT");
    }

    @Transactional(readOnly = true)
    public Page<StoreDto> getStoreOtherItemPage(SearchDto searchDto, StoreDto storeDto, Pageable pageable) {
        return itemRepository.getItemsByItemCategory(searchDto, storeDto, pageable, "OTHER");
    }

}
