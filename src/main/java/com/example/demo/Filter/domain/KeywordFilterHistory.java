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
@SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "UserInfo는 JPA 엔티티로, 내부 변경이 제한되며 Builder를 통한 저장도 안전합니다."
)
public class KeywordFilterHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordFilterHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter(lombok.AccessLevel.NONE)
    private UserInfo userInfo;                // 유저인포 테이블과 관계 맺기

    @ManyToOne(fetch = FetchType.LAZY)
    private Filter filter;                    // 필터 테이블과 관계 맺기

    private LocalDateTime usedAt;             // 필터 사용 시각

    private Boolean isUsed;                   // 필터 사용 여부



}
