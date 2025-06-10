package com.example.demo.review.domain;

import com.example.demo.auth.domain.UserInfo;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="review")
@Getter
@Setter
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class Review {

    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    // 단지 정보: 있을 수도, 없을 수도 있음  --> 없는 경우 매물 ID로 조회
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complex_id")
    private Complex complex;

    // 매물(property) 고유 ID
    @Column(name = "property_id", nullable = false)
    private Long propertyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private UserInfo user;   // 작성자

    @Column(name = "rating",precision = 2, scale = 1, nullable = false)
    private BigDecimal rating;

    private String content;

    @Column(name = "like_count")
    @ColumnDefault("0")
    private Long likeCount = 0L;

    @Column(name = "has_children")
    private boolean hasChildren;

    @Column(name = "is_resident")
    private boolean isResident;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;





    public boolean isMine(Long currentUserId){
        return user != null && user.getUserId().equals(currentUserId);
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
