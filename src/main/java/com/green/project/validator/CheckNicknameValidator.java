package com.green.project.validator;

import com.green.project.dto.member.MemberDto;
import com.green.project.entity.Member;
import com.green.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckNicknameValidator extends AbstractValidator<MemberDto> {

    private final MemberRepository memberRepository;
    @Override
    protected void doValidate(MemberDto dto, Errors errors) {
        Member member = Member.builder().email(dto.getNickname()).build();
        if (memberRepository.existsByNickname(member.getNickname())) {
            // 중복인 경우
            errors.rejectValue("nickname", "닉네임 중복 오류", "이미 사용 중인 닉네임입니다.");
        }
    }
}
