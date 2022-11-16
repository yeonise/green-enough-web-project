package com.green.project.controller;

import com.green.project.dto.CommunityDto;
import com.green.project.dto.DictionaryDto;
import com.green.project.dto.PostFormDto;
import com.green.project.dto.SearchDto;
import com.green.project.entity.Member;
import com.green.project.service.MemberService;
import com.green.project.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {

    private final PostService postService;

    private final MemberService memberService;

    @GetMapping(value = {"/notice", "/notice/{page}"})
    public String notice(SearchDto searchDto, CommunityDto communityDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 9);
        Page<CommunityDto> posts = postService.getNoticePostPage(searchDto, communityDto, pageable);
        model.addAttribute("posts", posts);
        model.addAttribute("communityDto", communityDto);
        model.addAttribute("maxPage", 3);
        return "community/notice";
    }

    @GetMapping(value = {"/question", "/question/{page}"})
    public String question(SearchDto searchDto, CommunityDto communityDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);
        Page<CommunityDto> posts = postService.getQuestionPostPage(searchDto, communityDto, pageable);
        model.addAttribute("posts", posts);
        model.addAttribute("communityDto", communityDto);
        model.addAttribute("maxPage", 3);
        return "community/question";
    }

    @GetMapping(value = {"/gallery", "/gallery/{page}"})
    public String gallery(SearchDto searchDto, CommunityDto communityDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<CommunityDto> posts = postService.getGalleryPostPage(searchDto, communityDto, pageable);
        model.addAttribute("posts", posts);
        model.addAttribute("communityDto", communityDto);
        model.addAttribute("maxPage", 3);
        return "community/gallery";
    }

    @GetMapping(value = {"/deal", "/deal/{page}"})
    public String deal(SearchDto searchDto, CommunityDto communityDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<CommunityDto> posts = postService.getDealPostPage(searchDto, communityDto, pageable);
        model.addAttribute("posts", posts);
        model.addAttribute("communityDto", communityDto);
        model.addAttribute("maxPage", 3);
        return "community/deal";
    }

    @GetMapping(value = {"/dictionary", "/dictionary/{page}"})
    public String dictionary(SearchDto searchDto, DictionaryDto dictionaryDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 1);
        Page<DictionaryDto> plants = postService.getPlantPostPage(searchDto, dictionaryDto, pageable);
        model.addAttribute("plants", plants);
        model.addAttribute("dictionaryDto", dictionaryDto);
        model.addAttribute("maxPage", 5);
        return "community/dictionary";
    }

    @GetMapping(value = "/post/new")
    public String postForm(Model model, Principal principal) {
        String writer = memberService.getNicknameByEmail(principal.getName());
        model.addAttribute("writer", writer);
        model.addAttribute("postFormDto", new PostFormDto());
        return "community/post-form";
    }

    @PostMapping(value = "/post/new")
    public String postNew(@Valid PostFormDto postFormDto, BindingResult bindingResult, Model model, Principal principal,
                          @RequestParam("postImgFile") List<MultipartFile> postImgFileList, HttpServletRequest request) {
        String writer = memberService.getNicknameByEmail(principal.getName());
        model.addAttribute("writer", writer);
        String referer = (String)request.getHeader("Referer");

        if (bindingResult.hasErrors()) { // 글 등록 중 필수 값이 없는 경우
            return "community/post-form";
        }
        try {
            postFormDto.setWriter(writer);
            Member member = memberService.getMemberByEmail(principal.getName());
            postService.savePost(member, postFormDto, postImgFileList); // 글 저장 로직 호출
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시물 등록 중 에러가 발생하였습니다.");
            return "community/post-form";
        }
        String category = "";
        if (postFormDto.getPostCategory().toString().equals("QUESTION")) {
            category = "community/question";
        } else if (postFormDto.getPostCategory().toString().equals("GALLERY")) {
            category = "community/gallery";
        } else if (postFormDto.getPostCategory().toString().equals("DEAL")) {
            category = "community/deal";
        } else {
            category = "community/notice";
        }
        return "redirect:/" + category;
    }

    // 게시글 삭제
    @PostMapping(value="/post/delete/{postId}")
    public String deletePost(@PathVariable("postId") Long postId) throws Exception {
        postService.deletePost(postId);
        return "redirect:/my-page/my-post";
    }

    @GetMapping(value = "/post/{postId}")
    public String postDetail(@PathVariable("postId") Long postId, Model model, Principal principal) {
        String writer = memberService.getNicknameByEmail(principal.getName());
        model.addAttribute("writer", writer);

        try {
            PostFormDto postFormDto = postService.getPostDetail(postId);
            model.addAttribute("postFormDto", postFormDto); // 조회한 데이터를 모델에 담아서 뷰로 전달하기
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 글입니다.");
            model.addAttribute("postFormDto", new PostFormDto());

            return "community/post-form";
        }
        return "community/post-form";
    }

    @PostMapping(value = "/post/{postId}")
    public String postUpdate(@Valid PostFormDto postFormDto, BindingResult bindingResult, Model model, Principal principal,
                             @RequestParam("postImgFile") List<MultipartFile> postImgFileList, HttpServletRequest request) {
        String writer = memberService.getNicknameByEmail(principal.getName());
        model.addAttribute("writer", writer);
        String referer = (String)request.getHeader("Referer");

        if (bindingResult.hasErrors()) { // 글 등록 중 필수 값이 없는 경우
            return "community/post-form";
        }
        try {
            postFormDto.setWriter(writer);
            postService.updatePost(postFormDto, postImgFileList); // 글 저장 로직 호출
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시물 수정 중 에러가 발생하였습니다.");
            return "community/post-form";
        }

        return "redirect:" + referer;
    }

    // 게시글 상세 페이지
    @GetMapping(value = "/post-detail/{postId}")
    public String postDetail(Model model, @PathVariable("postId") Long postId, Principal principal) {
        String user = memberService.getNicknameByEmail(principal.getName());
        PostFormDto postFormDto = postService.getPostDetail(postId);
        model.addAttribute("post", postFormDto);
        model.addAttribute("user", user);
        postService.updateHits(postId);
        return "community/post-detail";
    }

}
