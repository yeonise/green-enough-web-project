package com.green.project.entity;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String address; // 시 구 도로
    private String detail; // 상세주소

    protected Address() { }

    public Address(String address, String detail) {
        this.address = address;
        this.detail = detail;
    }

}

// 임베디드 타입을 적용하려면 새로운 클래스에 임베디드 타입으로 묶으려던 Attributes 를 넣어준 뒤 @Embeddable 을 붙인다.
// @Embeddable : 값 타입을 정의하는 곳에 표시
// @Embedded : 값 타입을 사용하는 곳에 표시