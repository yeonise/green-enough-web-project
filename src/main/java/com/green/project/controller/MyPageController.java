package com.green.project.controller;

import com.green.project.dto.MyPagePostDto;
import com.green.project.dto.member.BrandDto;
import com.green.project.dto.member.MemberDto;
import com.green.project.entity.Member;
import com.green.project.service.MemberService;
import com.green.project.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/my-page")
public class MyPageController {

    @Autowired
    PostService postService;

    @Autowired
    MemberService memberService;

    @GetMapping(value = "/info-member")
    public String memberInfoUpdate(Model model, Principal principal) {
        Member member = memberService.getMemberByEmail(principal.getName());
        model.addAttribute("member", member);

        try {
            MemberDto memberDto = memberService.getMemberDetail(principal.getName());
            model.addAttribute("memberDto", memberDto); // 조회한 데이터를 모델에 담아서 뷰로 전달하기
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 회원입니다.");
            return "/my-page/info-member";
        }
        return "/my-page/info-member";
    }

    @PostMapping(value = "/info-member")
    public String memberInfoUpdate(@Valid MemberDto memberDto, BindingResult bindingResult, Model model, Principal principal, HttpServletRequest request) {
        Member member = memberService.getMemberByEmail(principal.getName());
        model.addAttribute("member", member);

        String referer = (String)request.getHeader("Referer");

        if (bindingResult.hasErrors()) { // 필수 정보 값이 없는 경우
            return "/my-page/info-member";
        }
        if (!memberDto.getPassword().equals(memberDto.getPasswordCheck())) {
            model.addAttribute("passCheckErrorMsg", "입력하신 비밀번호가 일치하지 않습니다.");
            return "/my-page/info-member";
        }
        try {
            memberService.updateMember(memberDto); // 회원 저장 로직 호출
        } catch (Exception e) {
            model.addAttribute("errorMessage", "회원정보 수정 중 에러가 발생하였습니다.");
            return "/my-page/info-member";
        }

        return "my-page/modify-complete";
    }

    @GetMapping(value = "/info-brand")
    public String brandInfoUpdate(Model model, Principal principal) {
        Member member = memberService.getMemberByEmail(principal.getName());
        model.addAttribute("member", member);

        try {
            BrandDto brandDto = memberService.getBrandDetail(principal.getName());
            model.addAttribute("brandDto", brandDto); // 조회한 데이터를 모델에 담아서 뷰로 전달하기
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 회원입니다.");
            return "/my-page/info-brand";
        }

        return "/my-page/info-brand";
    }

    @PostMapping(value = "/info-brand")
    public String brandInfoUpdate(@Valid BrandDto brandDto, BindingResult bindingResult, Model model, Principal principal, HttpServletRequest request) {
        Member brand = memberService.getMemberByEmail(principal.getName());
        model.addAttribute("brand", brand);

        String referer = (String)request.getHeader("Referer");

        if (bindingResult.hasErrors()) { // 필수 정보 값이 없는 경우
            return "/my-page/info-brand";
        }
        if (!brandDto.getPassword().equals(brandDto.getPasswordCheck())) {
            model.addAttribute("passCheckErrorMsg", "입력하신 비밀번호가 일치하지 않습니다.");
            return "/my-page/info-brand";
        }
        try {
            memberService.updateBrand(brandDto); // 회원 저장 로직 호출
        } catch (Exception e) {
            model.addAttribute("errorMessage", "회원정보 수정 중 에러가 발생하였습니다.");
            return "/my-page/info-brand";
        }

        return "my-page/modify-complete";
    }

    @GetMapping(value = {"/my-post", "/my-post/{page}"})
    public String myPost(Principal principal, MyPagePostDto myPagePostDto, Model model, @PathVariable("page") Optional<Integer> page) {
        String nickname = memberService.getNicknameByEmail(principal.getName());
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);
        Page<MyPagePostDto> posts = postService.getMyPagePostPage(myPagePostDto, pageable, nickname);
        model.addAttribute("posts", posts);
        model.addAttribute("myPagePostDto", myPagePostDto);
        model.addAttribute("maxPage", 3);
        return "/my-page/my-post";
    }

    @GetMapping(value = "/sign-out")
    public String signOut() {
        return "/my-page/sign-out";
    }

}
