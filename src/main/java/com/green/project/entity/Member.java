package com.green.project.entity;

import com.green.project.constant.Role;
import com.green.project.dto.member.BrandDto;
import com.green.project.dto.member.MemberDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    @Embedded
    private Address address;

    private String phone;

    private String password;

    @Column(unique = true)
    private String brand;

    @Column(name = "reg_number", unique = true)
    private String regNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String provider;

    private String oauthId;

    public static Member createMember(MemberDto memberDto, PasswordEncoder passwordEncoder) {
        Address address = new Address(memberDto.getAddress(), memberDto.getDetail());
        String password = passwordEncoder.encode(memberDto.getPassword());
        Member member = Member.builder()
                .name(memberDto.getName())
                .nickname(memberDto.getNickname())
                .email(memberDto.getEmail())
                .address(address)
                .phone(memberDto.getPhone())
                .password(password)
                .provider("local")
                .role(Role.USER)
                .build();
        return member;
    }

    public static Member createBrand(BrandDto brandDto, PasswordEncoder passwordEncoder) {
        Address address = new Address(brandDto.getAddress(), brandDto.getDetail());
        String password = passwordEncoder.encode(brandDto.getPassword());
        Member member = Member.builder()
                .name(brandDto.getName())
                .email(brandDto.getEmail())
                .address(address)
                .phone(brandDto.getPhone())
                .password(password)
                .brand(brandDto.getBrand())
                .regNumber(brandDto.getRegNumber())
                .provider("local")
                .role(Role.BUSINESS)
                .build();
        return member;
    }

    public void updateMember(MemberDto memberDto, PasswordEncoder passwordEncoder) {
        Address address = new Address(memberDto.getAddress(), memberDto.getDetail());
        String password = passwordEncoder.encode(memberDto.getPassword());
        this.address = address;
        this.phone = memberDto.getPhone();
        this.password = password;
    }

    public void updateBrand(BrandDto brandDto, PasswordEncoder passwordEncoder) {
        Address address = new Address(brandDto.getAddress(), brandDto.getDetail());
        String password = passwordEncoder.encode(brandDto.getPassword());
        this.address = address;
        this.phone = brandDto.getPhone();
        this.password = password;
    }

}
