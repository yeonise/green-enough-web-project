package com.green.project.service;

import com.green.project.dto.*;
import com.green.project.dto.img.PostImgDto;
import com.green.project.entity.*;
import com.green.project.repository.PlantRepository;
import com.green.project.repository.PostImgRepository;
import com.green.project.repository.PostRepository;
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
public class PostService {

    private final PostRepository postRepository;
    private final PostImgRepository postImgRepository;
    private final PlantRepository plantRepository;
    private final PostImgService postImgService;

    public Long savePost(Member member, PostFormDto postFormDto, List<MultipartFile> postImgFileList) throws Exception {
        // 글 등록하기
        Post post = postFormDto.createPost();
        post.setMember(member);
        postRepository.save(post);

        // 이미지 등록하기
        for (int i = 0; i < postImgFileList.size(); i++) {
            PostImg postImg = new PostImg();
            postImg.setPost(post);
            if (i == 0) // 첫 번째 이미지의 대표 여부 값을 'Y'로 설정하기
                postImg.setRepImgYn("Y");
            else // 나머지 상품 이미지는 'N'로 설정하기
                postImg.setRepImgYn("N");
            postImgService.savePostImg(postImg, postImgFileList.get(i)); // 이미지 정보를 저장하기
        }

        return post.getId();
    }

    @Transactional(readOnly = true)
    public PostFormDto getPostDetail(Long postId) {
        List<PostImg> postImgList = postImgRepository.findByPostIdOrderByIdAsc(postId);
        List<PostImgDto> postImgDtoList = new ArrayList<>();

        for (PostImg postImg : postImgList) { // 조회한 PostImg 엔티티를 PostImgDto 객체로 만들어서 리스트에 추가하기
            PostImgDto postImgDto = PostImgDto.of(postImg);
            postImgDtoList.add(postImgDto);
        }

        Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new); // 게시글 아이디로 게시글 엔티티 조회하기
        PostFormDto postFormDto = PostFormDto.of(post);
        postFormDto.setPostImgDtoList(postImgDtoList);

        return postFormDto;
    }

    @Transactional(readOnly = true) // 내가 쓴 글 페이지에 게시글 불러오기
    public Page<MyPagePostDto> getMyPagePostPage(MyPagePostDto myPagePostDto,Pageable pageable, String nickname) {
        return postRepository.getMyPagePostPage(myPagePostDto, pageable, nickname);
    }

    public Long updatePost(PostFormDto postFormDto, List<MultipartFile> postImgFileList) throws Exception {
        // 게시글 수정
        Post post = postRepository.findById(postFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        post.updatePost(postFormDto);

        List<Long> postImgIds =  postFormDto.getPostImgIds();

        // 게시글 등록
        for (int i = 0; i < postImgFileList.size(); i++) {
            postImgService.updatePostImg(postImgIds.get(i), postImgFileList.get(i));
        }
        return post.getId();
    }

    @Transactional
    public void deletePost(Long postId) throws Exception {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id =" + postId));
        postRepository.delete(post);
    }

    // 조회수 증가
    @Transactional
    public int updateHits(Long id) {
        return postRepository.updateHits(id);
    }

    @Transactional(readOnly = true)
    public Page<CommunityDto> getNoticePostPage(SearchDto searchDto, CommunityDto communityDto, Pageable pageable) {
        return postRepository.getPostPageByCategory(searchDto, communityDto, pageable, "NOTICE");
    }

    @Transactional(readOnly = true)
    public Page<CommunityDto> getQuestionPostPage(SearchDto searchDto, CommunityDto communityDto, Pageable pageable) {
        return postRepository.getPostPageByCategory(searchDto, communityDto, pageable, "QUESTION");
    }

    @Transactional(readOnly = true)
    public Page<CommunityDto> getGalleryPostPage(SearchDto searchDto, CommunityDto communityDto, Pageable pageable) {
        return postRepository.getPostPageByCategory(searchDto, communityDto, pageable, "GALLERY");
    }

    @Transactional(readOnly = true)
    public Page<CommunityDto> getDealPostPage(SearchDto searchDto, CommunityDto communityDto, Pageable pageable) {
        return postRepository.getPostPageByCategory(searchDto, communityDto, pageable, "DEAL");
    }

    @Transactional(readOnly = true)
    public Page<DictionaryDto> getPlantPostPage(SearchDto searchDto, DictionaryDto dictionaryDto, Pageable pageable) {
        return plantRepository.getPlantPage(searchDto, dictionaryDto, pageable);
    }

}

