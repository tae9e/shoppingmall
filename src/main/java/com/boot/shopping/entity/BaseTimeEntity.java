package com.boot.shopping.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//abstract(추상),interface: 강제성
@Getter @Setter
@EntityListeners(value={AuditingEntityListener.class}) //Audit 적용
@MappedSuperclass   //공통 Mapping 필요할 때 상속받는 자식 클래스에만 Mapping 정보 제공
public abstract class BaseTimeEntity {
    @CreatedDate //Entity 생성 시 시간 자동 저장
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate   //Entity 값 변경 시 변경 시간 자동 저장
    private LocalDateTime updateTime;
}
