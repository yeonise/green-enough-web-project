package com.green.project.repository;

import com.green.project.dto.*;
import com.green.project.entity.QPost;
import com.green.project.entity.QPostImg;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {

        if (StringUtils.equals("title", searchBy)) {
            return QPost.post.title.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("writer", searchBy)) {
            return QPost.post.writer.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("content", searchBy)) {
            return QPost.post.content.like("%" + searchQuery + "%");
        }
        return null;
    }

    @Override
    public Page<CommunityDto> getPostPageByCategory(SearchDto searchDto, CommunityDto communityDto, Pageable pageable, String postCategory) {
        QPost post = QPost.post;
        QPostImg postImg = QPostImg.postImg;

        List<CommunityDto> content = queryFactory
                .select(new QCommunityDto(
                        post.id,
                        post.member,
                        post.title,
                        post.writer,
                        post.content,
                        postImg.imgUrl,
                        post.hits,
                        post.regTime)
                )
                .from(postImg)
                .where(post.postCategory.stringValue().eq(postCategory),
                        searchByLike(searchDto.getSearchBy(), searchDto.getSearchQuery()))
                .join(postImg.post, post)
                .where(postImg.repImgYn.eq("Y"))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count)
                .from(postImg)
                .where(post.postCategory.stringValue().eq(postCategory),
                        searchByLike(searchDto.getSearchBy(), searchDto.getSearchQuery()))
                .join(postImg.post, post)
                .where(postImg.repImgYn.eq("Y"))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MyPagePostDto> getMyPagePostPage(MyPagePostDto myPagePostDto, Pageable pageable, String nickname) {
        QPost post = QPost.post;

        List<MyPagePostDto> content = queryFactory
                .select(new QMyPagePostDto(
                        post.id,
                        post.postCategory,
                        post.title,
                        post.writer,
                        post.regTime,
                        post.hits)
                )
                .from(post)
                .where(post.writer.eq(nickname))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count)
                .from(post)
                .where(post.writer.eq(nickname))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

}
