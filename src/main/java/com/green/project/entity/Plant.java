package com.green.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이미지 소스 주소
    private String imgSrc;
    // 0. 한글 이름
    private String plantName;
    // 1. 식물학명
    private String plantName1;
    // 2. 식물영명
    private String plantName2;
    // 3. 유통명
    private String plantName3;
    // 5. 원산지 정보
    private String origin;
    // 6. 조언 정보
    private String advice;
    // 8. 성장 높이 정보
    private String height;
    // 9. 성장 넓이 정보
    private String width;
    // 10. 잎 형태 정보
    private String leafShape;
    // 13. 번식 시기 정보
    private String breeding;
    // 28. 기능성 정보
    @Column(length = 4000)
    private String functional;

}
