package com.example.demo.review.domain;

import com.example.demo.auth.domain.UserInfo;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "review_comment_like",
                uniqueConstraints = @UniqueConstraint(columnNames = {"comment_id","user_id"}))
@Getter
@Setter
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCommentLike {

    @Id
    @GeneratedValue
    @Column(name = "review_comment_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private ReviewComment reviewComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private UserInfo user;

    @Column(name = "is_liked", nullable = false)
    private boolean isLiked;   //매핑시 getIsxxx가 아닌 isXXX로 매핑됨

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //토글형식이므로 deletedAt 제거

    @PrePersist  //db insert 전 초기화
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate  //db update 전 업데이트
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

}
