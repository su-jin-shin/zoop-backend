package com.example.demo.review.domain;

import com.example.demo.auth.domain.UserInfo;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "review_like",
        uniqueConstraints = @UniqueConstraint(columnNames = {"review_id", "user_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private UserInfo user;

    //좋야요 상태 (토글용)
    @Column(name = "is_liked", nullable = false)
    @Builder.Default
    private boolean isLiked = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    /* 비즈니스 메서드 */

    // 좋아요 등록/해제
    public void updateLikeStatus(boolean isLiked) {
        this.isLiked = isLiked;
        this.updatedAt = LocalDateTime.now();
    }

    public static ReviewLike of(Review review, UserInfo user, boolean isLiked) {
        return ReviewLike.builder()
                .review(review)
                .user(user)
                .isLiked(isLiked)
                .build();
    }


}
