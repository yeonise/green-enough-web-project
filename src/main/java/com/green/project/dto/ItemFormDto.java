package com.green.project.dto;

import com.green.project.constant.ItemCategory;
import com.green.project.constant.ItemSellStatus;
import com.green.project.dto.img.ItemDetailImgDto;
import com.green.project.dto.img.ItemImgDto;
import com.green.project.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품 이름은 필수 입력 값입니다.")
    private String itemName;

    @NotBlank(message = "브랜드 이름은 필수 입력 값입니다.")
    private String itemBrand;

    @NotNull(message = "판매 가격은 필수 입력 값입니다.")
    private Integer price;

    @NotNull(message = "상품 재고는 필수 입력 값입니다.")
    private Integer stock;

    private ItemSellStatus itemSellStatus;

    private ItemCategory itemCategory;

//    @NotNull(message = "상품 이미지는 필수 입력 값입니다.")
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); // 상품 이미지

//    @NotNull(message = "상품 상세 이미지는 필수 입력 값입니다.")
    private List<ItemDetailImgDto> itemDetailImgDtoList = new ArrayList<>(); // 상품 상세 설명 이미지

    private List<Long> itemImgIds = new ArrayList<>();

    private List<Long> itemDetailImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    // DTO 객체를 Entity 객체로 변환하기
    public Item createItem(){
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
        return modelMapper.map(this, Item.class);
    }

    // Entity 객체를 DTO 객체로 변환하기
    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }

}
