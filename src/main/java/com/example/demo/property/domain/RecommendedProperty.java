package com.example.demo.property.domain;

import com.example.demo.Filter.domain.Filter;
import com.example.demo.auth.domain.UserInfo;
import com.example.demo.chat.domain.Message;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@SuppressFBWarnings(
        value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" }
)
//추후 수정예정
@Entity
@Table(
        name = "recommended_property",
//        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","property_id"})
        uniqueConstraints = @UniqueConstraint(columnNames = {"property_id", "message_id"})
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendedItemId; //추천매물 아이디(기본키)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property; //매물 아이디(외래키)

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private UserInfo userInfo; //유저 아이디(외래키)
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "filter_id")
//    private Filter filter; //필터 아이디(외래키)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message; //메시지 아이디(외래키)

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt; //추천 매물 추가 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt; //추천 매물 수정 시각

}
