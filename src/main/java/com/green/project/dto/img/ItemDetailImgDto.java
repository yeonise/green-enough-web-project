package com.green.project.dto.img;

import com.green.project.entity.ItemDetailImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemDetailImgDto {

    private Long id;
    private String itemImgName;
    private String oriImgName;
    private String imgUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemDetailImgDto of(ItemDetailImg itemDetailImg) {
        return modelMapper.map(itemDetailImg, ItemDetailImgDto.class);
    }

}
