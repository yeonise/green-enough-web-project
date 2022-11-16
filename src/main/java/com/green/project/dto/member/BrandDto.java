package com.green.project.dto.member;

import com.green.project.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class BrandDto {
    @NotBlank(message = "사업자 이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "사업장 주소는 필수 입력 값입니다.")
    private String address;
    private String detail;

    @NotEmpty(message = "휴대폰 번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
    private String phone;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 1개 이상 포함하여 8자 이상 16자 이하로 입력해주세요.")
    private String password;

    @NotEmpty(message = "입력하신 비밀번호를 확인해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "입력하신 비밀번호를 확인해주세요.")
    private String passwordCheck;

    @NotBlank(message = "회사명은 필수 입력 값입니다.")
    private String brand;

    @NotEmpty(message = "사업자 등록 번호는 필수 입력 값입니다.")
    @Length(min = 10, max = 10, message = "올바른 사업자 등록 번호를 입력해주세요.")
    private String regNumber;

    private static ModelMapper modelMapper = new ModelMapper();

    // DTO 객체를 Entity 객체로 변환하기
    public Member createBrand() {
        return modelMapper.map(this, Member.class);
    }

    // Entity 객체를 DTO 객체로 변환하기
    public static BrandDto of(Member member) {
        BrandDto getBrandDto = modelMapper.map(member, BrandDto.class);
        getBrandDto.setDetail(member.getAddress().getDetail());

        return getBrandDto;
    }
}
