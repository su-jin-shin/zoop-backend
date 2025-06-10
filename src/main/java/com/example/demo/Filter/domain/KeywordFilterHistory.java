package com.example.demo.Filter.domain;

import com.example.demo.auth.domain.UserInfo;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuppressFBWarnings(
        value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" },
        justification = "UserInfo는 JPA 엔티티이며, setter 없이 관리되어 안전하게 노출됩니다."
)
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
