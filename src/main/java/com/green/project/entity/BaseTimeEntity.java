package com.green.project.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value = AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regTime; // 등록한 시간

    @LastModifiedDate
    private LocalDateTime updateTime; // 가장 최근에 수정한 시간
}

// @EntityListeners JPA Entity 에서 이벤트가 발생할 때마다 특정 로직을 실행시킬 수 있다.
// Entity Listener 엔티티의 변화를 감지하고 테이블의 데이터를 조작하는 일을 한다.
// 데이터를 생성 또는 수정함에 따라 createdAt, updatedAt을 업데이트 하는 것은 대부분의 데이터베이스에서 사용한다. 그래서 JPA 는 이러한 기능을 기본적으로 제공하고 있다.
// AuditingEntityListener.class 를 사용하기 위해서는 ~Application.java 파일에 @EnableJpaAuditing 을 추가해야 한다.
// Entity 로 사용할 객체의 @EntityListeners 에는 value 값에 AuditingEntityListener.class 를 넣는다.
// 엔티티는 엔티티만 상속받을 수 있기 때문에 엔티티가 아닌 클래스를 상속받게 하기 위해서는 @MappedSuperclass 를 사용해야 한다.
