package com.green.project.controller;

import com.green.project.dto.member.BrandDto;
import com.green.project.dto.member.MemberDto;
import com.green.project.entity.Member;
import com.green.project.service.MemberService;
import com.green.project.validator.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/login")
    public String loginMember() {
        return "member/login";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "이메일 또는 비밀번호를 확인해주세요.");
        return "member/login";
    }

    @GetMapping(value = "/register")
    public String register() {
        return "member/register";
    }

    // 일반 회원 등록
    @GetMapping(value = "/register/member")
    public String registerMember(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "member/member-register";
    }

    @PostMapping(value = "/register/member")
    public String registerMember(@Valid MemberDto memberDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/member-register";
        }

        if (!memberDto.getPassword().equals(memberDto.getPasswordCheck())) {
            model.addAttribute("passCheckErrorMsg", "입력하신 비밀번호가 일치하지 않습니다.");
        }
        if (memberService.checkEmailDuplicate(memberDto.getEmail()) == true) {
            model.addAttribute("emailCheckErrorMsg", "이미 등록된 이메일입니다.");
        }
        if (memberService.checkNicknameDuplicate(memberDto.getNickname()) == true) {
            model.addAttribute("nicknameCheckErrorMsg", "이미 사용 중인 닉네임입니다.");
        }
        if (!memberDto.getPassword().equals(memberDto.getPasswordCheck()) || memberService.checkEmailDuplicate(memberDto.getEmail()) == true || memberService.checkNicknameDuplicate(memberDto.getNickname()) == true) {
            return "member/member-register";
        }

        try {
            Member member = Member.createMember(memberDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/member-register";
        }
        return "member/register-complete";
    }

    // 사업자 회원 등록
    @GetMapping(value = "/register/brand")
    public String registerBrand(Model model) {
        model.addAttribute("brandDto", new BrandDto());
        return "member/brand-register";
    }

    @PostMapping(value = "/register/brand")
    public String registerBrand(@Valid BrandDto brandDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/brand-register";
        }

        if (!brandDto.getPassword().equals(brandDto.getPasswordCheck())) {
            model.addAttribute("passCheckErrorMsg", "입력하신 비밀번호가 일치하지 않습니다.");
        }
        if (memberService.checkEmailDuplicate(brandDto.getEmail()) == true) {
            model.addAttribute("emailCheckErrorMsg", "이미 등록된 이메일입니다.");
        }
        if (memberService.checkBrandDuplicate(brandDto.getBrand()) == true) {
            model.addAttribute("brandCheckErrorMsg", "이미 등록된 회사명입니다.");
        }
        if (memberService.checkRegNumberDuplicate(brandDto.getRegNumber()) == true) {
            model.addAttribute("regNumCheckErrorMsg", "이미 등록된 사업자 번호입니다.");
        }
        if (!brandDto.getPassword().equals(brandDto.getPasswordCheck()) || memberService.checkEmailDuplicate(brandDto.getEmail()) == true || memberService.checkBrandDuplicate(brandDto.getBrand()) == true || memberService.checkRegNumberDuplicate(brandDto.getRegNumber()) == true) {
            return "member/brand-register";
        }

        try {
            Member member = Member.createBrand(brandDto, passwordEncoder);
            memberService.saveBrand(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/brand-register";
        }
        return "member/register-complete";
    }

    // 중복 확인하기
    private final CheckMemberEmailValidator checkMemberEmailValidator;
    private final CheckNicknameValidator checkNicknameValidator;
    private final CheckBrandEmailValidator checkBrandEmailValidator;
    private final CheckBrandValidator checkbrandValidator;
    private final CheckRegNumberValidator checkRegNumberValidator;

    @InitBinder("MemberDto")
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkNicknameValidator);
        binder.addValidators(checkMemberEmailValidator);
    }

    @InitBinder("BrandDto")
    public void validatorBrandBinder(WebDataBinder binder) {
        binder.addValidators(checkBrandEmailValidator);
        binder.addValidators(checkbrandValidator);
        binder.addValidators(checkRegNumberValidator);
    }

}
