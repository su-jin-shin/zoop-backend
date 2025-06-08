package com.example.demo.Filter.domain;

import com.example.demo.auth.domain.UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class KeywordFilterHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordFilterHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserInfo userInfo;                // 유저인포 테이블과 관계 맺기

    @ManyToOne(fetch = FetchType.LAZY)
    private Filter filter;                    // 필터 테이블과 관계 맺기

    private LocalDateTime usedAt;             // 필터 사용 시각

    private Boolean isUsed;                   // 필터 사용 여부



}
