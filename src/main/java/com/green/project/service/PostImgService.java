package com.green.project.service;

import com.green.project.entity.PostImg;
import com.green.project.repository.PostImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

// 게시글 이미지를 업로드하고, 게시글 이미지 정보를 저장하는 클래스
@Service
@Transactional
@RequiredArgsConstructor
public class PostImgService {

    @Value("${postImgLocation}")
    private String postImgLocation;

    private final PostImgRepository postImgRepository;

    private final FileService fileService;

    public void savePostImg(PostImg postImg, MultipartFile postImgFile) throws Exception {
        String oriImgName = postImgFile.getOriginalFilename();
        String postImgName = "";
        String imgUrl = "";

        // 파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            postImgName = fileService.uploadFile(postImgLocation, oriImgName, postImgFile.getBytes());
            // 사용자가 이미지를 등록했다면 저장할 경로와 파일의 이름과 파일의 바이트 배열을 파일 업로드 파라미터로 uploadFile 메소드를 호출
            // 호출 결과 로컬에 저장된 파일의 이름을 postImgName 변수에 저장
            imgUrl = "/images/post/" + postImgName; // 저장한 이미지를 불러올 경로를 설정. uploadPath 경로 아래 이미지를 저장하므로 'post' 똑같은 이름을 사용
        }

        // 게시글 이미지 정보 저장
        postImg.updateItemImg(oriImgName, postImgName, imgUrl); // 입력받은 이미지 정보를 저장
        postImgRepository.save(postImg);
    }

    public void updatePostImg(Long postImgId, MultipartFile postImgFile) throws Exception {
        if (!postImgFile.isEmpty()) {
            PostImg savedPostImg = postImgRepository.findById(postImgId).orElseThrow(EntityNotFoundException::new);

            // 기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedPostImg.getPostImgName())) {
                fileService.deleteFile(postImgLocation + "/" + savedPostImg.getPostImgName());
        }
            String oriImgName = postImgFile.getOriginalFilename();
            String postImgName = fileService.uploadFile(postImgLocation, oriImgName, postImgFile.getBytes());
            String imgUrl = "/images/post/" + postImgName;

            savedPostImg.updateItemImg(oriImgName, postImgName, imgUrl);
        }
    }
}
