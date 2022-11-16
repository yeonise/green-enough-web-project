package com.green.project.validator;

import com.green.project.dto.member.BrandDto;
import com.green.project.entity.Member;
import com.green.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckBrandValidator extends AbstractValidator<BrandDto> {

    private final MemberRepository memberRepository;
    @Override
    protected void doValidate(BrandDto brandDto, Errors errors) {
        Member member = Member.builder().regNumber(brandDto.getBrand()).build();
        if (memberRepository.existsByBrand(member.getBrand())) {
            // 중복인 경우
            errors.rejectValue("brand", "회사명 중복 오류", "이미 등록된 회사명입니다.");
        }
    }
}
