package com.green.project.validator;

import com.green.project.dto.member.MemberDto;
import com.green.project.entity.Member;
import com.green.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckMemberEmailValidator extends AbstractValidator<MemberDto> {

    private final MemberRepository memberRepository;
    @Override
    protected void doValidate(MemberDto dto, Errors errors) {
        Member member = Member.builder().email(dto.getEmail()).build();
        if (memberRepository.existsByEmail(member.getEmail())) {
            // 중복인 경우
            errors.rejectValue("email", "이메일 중복 오류", "이미 등록된 이메일입니다.");
        }
    }
}
