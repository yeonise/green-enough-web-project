package com.green.project.oauth.user;

import java.util.Map;

public interface OAuthUserInfo {
    Map<String, Object> getAttributes();
    String getOAuthId();
    String getProvider();
    String getEmail();
    String getName();
    String getNickname();
    String getMobile();
}
