package com.green.project.oauth;

import com.green.project.constant.Role;
import com.green.project.entity.Member;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String nickname;

    private Role role = Role.USER;

    public SessionUser(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }
}
