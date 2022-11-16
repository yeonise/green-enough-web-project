package com.green.project.dto;

import com.green.project.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
    private String searchDateType; // 조회 시간 기준
    private ItemSellStatus searchSellStatus; // 판매 상태를 기준으로 데이터 조회
    private String searchBy; // 조회할 유형
    private String searchQuery; // 조회할 검색어
}
