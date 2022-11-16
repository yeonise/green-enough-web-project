package com.green.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadItemPath}")
    String uploadItemPath; // 상품 관련 파일 경로

    @Value("${uploadPostPath}")
    String uploadPostPath; // 게시글 관련 파일 경로

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadItemPath)
                .addResourceLocations(uploadPostPath);
    }

}

// 파일을 읽어올 경로를 설정
// addResourceHandlers 메소드를 통해서 로컬 컴퓨터에 업로드한 파일을 찾을 위치를 설정

