package com.example.demo.Filter.domain;

import com.example.demo.auth.domain.UserInfo;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
    @Getter(lombok.AccessLevel.NONE)
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "JPA Entity 연관관계 필드는 ORM에 의해 관리되므로 직접적인 노출 위험이 낮습니다.")
    private UserInfo userInfo;                // 유저인포 테이블과 관계 맺기

    @ManyToOne(fetch = FetchType.LAZY)
    private Filter filter;                    // 필터 테이블과 관계 맺기

    private LocalDateTime usedAt;             // 필터 사용 시각

    private Boolean isUsed;                   // 필터 사용 여부



}
