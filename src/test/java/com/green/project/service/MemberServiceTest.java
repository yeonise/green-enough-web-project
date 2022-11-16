package com.green.project.service;

import com.green.project.dto.member.BrandDto;
import com.green.project.dto.member.MemberDto;
import com.green.project.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMemberDto() {
        MemberDto memberDto = new MemberDto();
        memberDto.setName("이병건");
        memberDto.setNickname("이말년");
        memberDto.setEmail("zilioner@email.com");
        memberDto.setAddress("서울시 마포구");
        memberDto.setDetail("작업실 101호");
        memberDto.setPassword("qudrjs123!");
        memberDto.setPasswordCheck("qudrus123!");
        memberDto.setPhone("01050009000");

        return Member.createMember(memberDto, passwordEncoder);
    }

    public Member createBrandDto() {
        BrandDto brandDto = new BrandDto();
        brandDto.setName("주호민");
        brandDto.setEmail("noizemasta@email.com");
        brandDto.setAddress("서울시 영등포구");
        brandDto.setDetail("작업실 201호");
        brandDto.setPassword("ghals123!");
        brandDto.setPasswordCheck("ghals123!");
        brandDto.setPhone("01020006000");
        brandDto.setBrand("펄이 빛나는 밤에");
        brandDto.setRegNumber("1020030040");

        return Member.createBrand(brandDto, passwordEncoder);
    }

//    @Test
//    @DisplayName("일반 회원 등록 테스트")
    public void createMemberTest() {
        Member member = createMemberDto();
        Member savedMember = memberService.saveMember(member);

        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getNickname(), savedMember.getNickname());
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getPassword(), savedMember.getPassword());
    }

//    @Test
//    @DisplayName("사업자 회원 등록 테스트")
    public void createBrandTest() {
        Member member = createBrandDto();
        Member savedMember = memberService.saveBrand(member);

        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getBrand(), savedMember.getBrand());
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRegNumber(), savedMember.getRegNumber());
    }

    @Test
    @DisplayName("이메일 중복 회원 가입 테스트")
    public void emailDuplicateMemberTest() {
        Member member = createMemberDto();

        Throwable throwable = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member);
        });

        assertEquals("이미 등록된 이메일입니다.", throwable.getMessage());
    }
}
