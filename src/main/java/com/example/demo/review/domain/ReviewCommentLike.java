package com.example.demo.review.domain;

import com.example.demo.auth.domain.UserInfo;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "review_comment_like",
                uniqueConstraints = @UniqueConstraint(columnNames = {"comment_id","user_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_comment_like_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private ReviewComment reviewComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private UserInfo user;

    @Column(name = "is_liked", nullable = false)
    @Builder.Default
    private boolean isLiked = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate  //db update 전 업데이트
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    // 좋아요 등록/해제 
    public void updateLikeStatus(boolean isLiked){
        this.isLiked = isLiked;
        onUpdate();
    }

}
