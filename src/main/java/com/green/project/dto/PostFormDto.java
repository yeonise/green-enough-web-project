package com.green.project.dto;

import com.green.project.constant.PostCategory;
import com.green.project.dto.img.PostImgDto;
import com.green.project.entity.Post;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostFormDto {

    private Long id;

    @NotBlank(message = "글 제목을 입력해주세요.")
    private String title;

    private String writer;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private PostCategory postCategory;

    private int hits;

    private List<PostImgDto> postImgDtoList = new ArrayList<>(); // 첨부 이미지

    private List<Long> postImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    // DTO 객체를 Entity 객체로 변환하기
    public Post createPost() {
        return modelMapper.map(this, Post.class);
    }

    // Entity 객체를 DTO 객체로 변환하기
    public static PostFormDto of(Post post) {
        return modelMapper.map(post, PostFormDto.class);
    }
}
