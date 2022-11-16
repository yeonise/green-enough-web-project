package com.green.project.validator;

import com.green.project.dto.member.BrandDto;
import com.green.project.entity.Member;
import com.green.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckRegNumberValidator extends AbstractValidator<BrandDto> {

    private final MemberRepository memberRepository;
    @Override
    protected void doValidate(BrandDto brandDto, Errors errors) {
        Member member = Member.builder().regNumber(brandDto.getRegNumber()).build();
        if (memberRepository.existsByRegNumber(member.getRegNumber())) {
            // 중복인 경우
            errors.rejectValue("regNumber", "사업자 등록 번호 중복 오류", "이미 등록된 사업자 등록 번호입니다.");
        }
    }
}
