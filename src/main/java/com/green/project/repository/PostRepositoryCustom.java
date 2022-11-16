package com.green.project.repository;

import com.green.project.dto.CommunityDto;
import com.green.project.dto.MyPagePostDto;
import com.green.project.dto.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<CommunityDto> getPostPageByCategory(SearchDto searchDto, CommunityDto communityDto, Pageable pageable, String postCategory);

    Page<MyPagePostDto> getMyPagePostPage(MyPagePostDto myPagePostDto, Pageable pageable, String nickname);

}
