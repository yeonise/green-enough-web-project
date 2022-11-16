package com.green.project.dto.board;

import com.green.project.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommunityBoardDto {

    private Long id;
    private Member member;
    private String title;
    private String writer;
    private String content;
    private Integer hits;
    private String imgUrl;
    private LocalDateTime regTime;

    @QueryProjection
    public CommunityBoardDto(Long id, Member member, String title, String writer, String content, String imgUrl, Integer hits, LocalDateTime regTime) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.imgUrl = imgUrl;
        this.hits = hits;
        this.regTime = regTime;
    }
}
