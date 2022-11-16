package com.green.project.service;

import com.green.project.dto.member.BrandDto;
import com.green.project.dto.member.MemberDto;
import com.green.project.entity.Item;
import com.green.project.entity.Member;
import com.green.project.entity.Post;
import com.green.project.repository.ItemRepository;
import com.green.project.repository.MemberRepository;
import com.green.project.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final PostRepository postRepository;

    private final ItemRepository itemRepository;

    private final PasswordEncoder passwordEncoder;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        validateDuplicateNickName(member);
        return memberRepository.save(member);
    }

    public Member saveBrand(Member member) {
        validateDuplicateMember(member);
        validateDuplicateBrand(member);
        return memberRepository.save(member);
    }

    // 회원탈퇴
    public void deleteMember(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        // 작성한 게시물이 있을 때
        if (postRepository.existsByWriter(member.get().getNickname())) {
            List<Post> postList = postRepository.findByWriter(member.get().getNickname());
            for (int i = 0; i < postList.size(); i++) {
                postRepository.deleteById(postList.get(i).getId()); // 등록한 게시물 삭제
            }
        }
        // 등록한 상품이 있을 경우
        if (itemRepository.existsByItemBrand(member.get().getBrand())) {
            List<Item> itemList = itemRepository.findByItemBrandOrderByPriceDesc(member.get().getBrand());
            for (int i = 0; i < itemList.size(); i++) {
                itemRepository.deleteById(itemList.get(i).getId()); // 등록한 상품 삭제
            }
        }
        memberRepository.deleteById(id); // 회원 삭제
    }

    public String getBrandByEmail(String email) {
        return memberRepository.findByEmail(email).getBrand();
    }

    public String getNicknameByEmail(String email) {
        return memberRepository.findByEmail(email).getNickname();
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }


    public void validateDuplicateMember(Member member) {
        if (memberRepository.existsByEmail(member.getEmail()) == true) {
            throw new IllegalStateException("이미 등록된 이메일입니다.");
        }
    }

    public void validateDuplicateNickName(Member member) {
        if (memberRepository.existsByNickname(member.getNickname()) == true) {
            throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
        }
    }

    public void validateDuplicateBrand(Member member) {
        if (memberRepository.existsByBrand(member.getBrand()) == true) {
            throw new IllegalStateException("이미 등록된 회사명입니다.");
        }
    }

    public void validateDuplicateRegNumber(Member member) {
        if (memberRepository.existsByRegNumber(member.getRegNumber()) == true) {
            throw new IllegalStateException("이미 등록된 사업자 번호입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    public boolean checkEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email);
    }
    public boolean checkNicknameDuplicate(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
    public boolean checkBrandDuplicate(String brand) { return memberRepository.existsByBrand(brand); }
    public boolean checkRegNumberDuplicate(String regNumber) { return memberRepository.existsByRegNumber(regNumber); }

    public Long updateMember(MemberDto memberDto) throws Exception {
        // 회원 수정
        Member member = memberRepository.findByEmail(memberDto.getEmail());
        member.updateMember(memberDto, passwordEncoder);

        return member.getId();
    }

    public Long updateBrand(BrandDto brandDto) throws Exception {
        // 회원 수정
        Member member = memberRepository.findByEmail(brandDto.getEmail());
        member.updateBrand(brandDto, passwordEncoder);

        return member.getId();
    }

    @Transactional(readOnly = true)
    public MemberDto getMemberDetail(String email) {

        Member member = memberRepository.findByEmail(email);
        MemberDto memberDto = MemberDto.of(member);

        return memberDto;
    }

    @Transactional(readOnly = true)
    public BrandDto getBrandDetail(String email) {

        Member member = memberRepository.findByEmail(email);
        BrandDto brandDto = BrandDto.of(member);

        return brandDto;
    }
}
