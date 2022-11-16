package com.green.project.service;

import com.green.project.entity.ItemDetailImg;
import com.green.project.repository.ItemDetailImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

// 상품 상세 이미지를 업로드하고, 상품 상세 이미지 정보를 저장하는 클래스
@Service
@Transactional
@RequiredArgsConstructor
public class ItemDetailImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemDetailImgRepository itemDetailImgRepository;

    private final FileService fileService;

    public void saveItemDetailImg(ItemDetailImg itemDetailImg, MultipartFile itemDetailImgFile) throws Exception {
        String oriImgName = itemDetailImgFile.getOriginalFilename();
        String itemDetailImgName = "";
        String imgUrl = "";

        // 파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            itemDetailImgName = fileService.uploadFile(itemImgLocation, oriImgName, itemDetailImgFile.getBytes());
            // 사용자가 상품의 상세 이미지를 등록했다면 저장할 경로와 파일의 이름과 파일의 바이트 배열을 파일 업로드 파라미터로 uploadFile 메소드를 호출
            // 호출 결과 로컬에 저장된 파일의 이름을 itemDetailImgName 변수에 저장
            imgUrl = "/images/item/" + itemDetailImgName; // 저장한 상품 이미지를 불러올 경로를 설정. uploadPath 경로 아래 이미지를 저장하므로 'item' 똑같은 이름을 사용
        }

        // 상품 상세 이미지 정보 저장
        itemDetailImg.updateItemDetailImg(oriImgName, itemDetailImgName, imgUrl); // 입력받은 상품 상세 이미지 정보를 저장
        itemDetailImgRepository.save(itemDetailImg);
    }

    public void updateItemDetailImg(Long itemDetailImgId, MultipartFile itemDetailImgFile) throws Exception {
        if (!itemDetailImgFile.isEmpty()) {
            ItemDetailImg savedItemDetailImg = itemDetailImgRepository.findById(itemDetailImgId).orElseThrow(EntityNotFoundException::new);

            // 기존 상세 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedItemDetailImg.getItemDetailImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemDetailImg.getItemDetailImgName());
            }
            String oriImgName = itemDetailImgFile.getOriginalFilename();
            String itemDetailImgName = fileService.uploadFile(itemImgLocation, oriImgName, itemDetailImgFile.getBytes());
            String imgUrl = "/images/item/" + itemDetailImgName;

            savedItemDetailImg.updateItemDetailImg(oriImgName, itemDetailImgName, imgUrl);
        }
    }
}
