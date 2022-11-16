package com.green.project.service;

import com.green.project.constant.ItemCategory;
import com.green.project.constant.ItemSellStatus;
import com.green.project.constant.PostCategory;
import com.green.project.dto.ItemFormDto;
import com.green.project.dto.PostFormDto;
import com.green.project.entity.*;
import com.green.project.repository.MemberRepository;
import com.green.project.repository.PostImgRepository;
import com.green.project.repository.PostRepository;
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
public class PostServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostImgRepository postImgRepository;

    @Autowired
    PostService postService;

    List<MultipartFile> createMultipartFiles() throws Exception {

        List<MultipartFile> multipartFileList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String path = "/Users/yeon/Documents/project/post";
            String imageName = "image" + i +".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
            multipartFileList.add(multipartFile);
        }
        return multipartFileList;
    }

    public Member saveMember() {
        Member member = Member.builder()
                .email("test@email.com")
                .nickname("테스트 유저 닉네임")
                .build();
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("게시글 등록 테스트")
    void savePost() throws Exception {
        Member member = saveMember();

        PostFormDto postFormDto = new PostFormDto();
        postFormDto.setTitle("테스트 게시글 제목");
        postFormDto.setWriter("테스트 유저 닉네임");
        postFormDto.setContent("테스트 게시글 내용");
        postFormDto.setPostCategory(PostCategory.QUESTION);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long postId = postService.savePost(member, postFormDto, multipartFileList);

        List<PostImg> postImgList = postImgRepository.findByPostIdOrderByIdAsc(postId);

        Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);

        assertEquals(postFormDto.getTitle(), post.getTitle());
        assertEquals(postFormDto.getWriter(), member.getNickname());
        assertEquals(postFormDto.getContent(), post.getContent());
        assertEquals(postFormDto.getPostCategory(), post.getPostCategory());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), postImgList.get(0).getOriImgName());
    }
}
