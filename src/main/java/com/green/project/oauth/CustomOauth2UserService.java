package com.green.project.oauth;

import com.green.project.constant.Role;
import com.green.project.entity.Member;
import com.green.project.oauth.user.GoogleUserInfo;
import com.green.project.oauth.user.NaverUserInfo;
import com.green.project.oauth.user.OAuthUserInfo;
import com.green.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        OAuthUserInfo oAuth2UserInfo = null;
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId();

        if (provider.equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (provider.equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        }

        String oauthId = oAuth2UserInfo.getOAuthId();
        String name = oAuth2UserInfo.getName();
        String email = oAuth2UserInfo.getEmail();
        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = passwordEncoder.encode("비밀번호!" + uuid);
        Role role = Role.USER;

        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            if (provider.equals("naver")) {
                String nickname = oAuth2UserInfo.getNickname().replaceAll(Pattern.quote(" "), "");
                String mobile = oAuth2UserInfo.getMobile().replaceAll(Pattern.quote("-"), "");

                member = Member.builder()
                        .oauthId(oauthId)
                        .provider(provider)
                        .name(name)
                        .nickname(nickname)
                        .email(email)
                        .phone(mobile)
                        .password(password)
                        .role(role)
                        .build();

            } else if (provider.equals("google")) {
                member = Member.builder()
                        .oauthId(oauthId)
                        .provider(provider)
                        .name(name)
                        .email(email)
                        .password(password)
                        .role(role)
                        .build();
            }

            memberRepository.save(member);
        }
        return new PrincipalDetails(member, oAuth2UserInfo);
    }

}
