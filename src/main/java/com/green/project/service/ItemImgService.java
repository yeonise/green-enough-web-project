package com.green.project.service;

import com.green.project.entity.ItemImg;
import com.green.project.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

// 상품 이미지를 업로드하고, 상품 이미지 정보를 저장하는 클래스
@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String itemImgName = "";
        String imgUrl = "";

        // 파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            itemImgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            // 사용자가 상품의 이미지를 등록했다면 저장할 경로와 파일의 이름과 파일의 바이트 배열을 파일 업로드 파라미터로 uploadFile 메소드를 호출
            // 호출 결과 로컬에 저장된 파일의 이름을 itemImgName 변수에 저장
            imgUrl = "/images/item/" + itemImgName; // 저장한 상품 이미지를 불러올 경로를 설정. uploadPath 경로 아래 이미지를 저장하므로 'item' 똑같은 이름을 사용
        }

        // 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, itemImgName, imgUrl); // 입력받은 상품 이미지 정보를 저장
        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if (!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);

            // 기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getItemImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getItemImgName());
        }
            String oriImgName = itemImgFile.getOriginalFilename();
            String itemImgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + itemImgName;

            savedItemImg.updateItemImg(oriImgName, itemImgName, imgUrl);
        }
    }
}
