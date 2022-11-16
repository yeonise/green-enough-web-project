package com.green.project.dto.img;

import com.green.project.entity.PostImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class PostImgDto {

    private Long id;
    private String itemImgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static PostImgDto of(PostImg postImg) {
        return modelMapper.map(postImg, PostImgDto.class);
    }

}
