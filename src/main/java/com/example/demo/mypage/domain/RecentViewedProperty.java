package com.example.demo.mypage.domain;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.property.domain.Property;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "recent_viewed_property", uniqueConstraints = {
        @UniqueConstraint(name = "uniq_user_property", columnNames = {"user_id", "property_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentViewedProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recent_viewed_property_id")
    private Long recentViewedPropertyId;

    @SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification = "UserInfo is managed entity and lifecycle is controlled by JPA")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;

    @UpdateTimestamp
    @Column(name = "viewed_at", nullable = false)
    private LocalDateTime viewedAt;

    private LocalDateTime deletedAt; // 확장 고려한 컬럼

    public static class RecentViewedPropertyBuilder {
        @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "UserInfo는 JPA 관리 객체로 안전하게 공유 가능")
        public RecentViewedProperty.RecentViewedPropertyBuilder user(UserInfo user) {
            this.user = user;
            return this;
        }
    }
}
