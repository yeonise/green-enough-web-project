package com.green.project.dto;

import com.green.project.constant.PostCategory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class MyPagePostDto {
    private Long postId;
    private PostCategory postCategory;
    private String title;
    private String writer;
    private LocalDateTime regTime;
    private Integer hits;

    @QueryProjection
    public MyPagePostDto(Long postId, PostCategory postCategory, String title, String writer, LocalDateTime regTime, Integer hits) {
        this.postId = postId;
        this.postCategory = postCategory;
        this.title = title;
        this.writer = writer;
        this.regTime = regTime;
        this.hits = hits;
    }

}
