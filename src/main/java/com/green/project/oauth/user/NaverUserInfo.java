package com.green.project.oauth.user;

import java.util.Map;

public class NaverUserInfo implements OAuthUserInfo {

    private Map<String, Object> attributes; // OAuth2User.getAttributes();
    private Map<String, Object> attributesResponse;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributesResponse = (Map<String, Object>) attributes.get("response");
        this.attributes = (Map<String, Object>) attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getOAuthId() {
        return attributesResponse.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return attributesResponse.get("email").toString();
    }

    @Override
    public String getName() {
        return attributesResponse.get("name").toString();
    }

    @Override
    public String getNickname() {
        return attributesResponse.get("nickname").toString();
    }
    @Override
    public String getMobile() {
        return attributesResponse.get("mobile").toString();
    }

}
