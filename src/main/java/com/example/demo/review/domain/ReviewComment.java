package com.example.demo.review.domain;


import com.example.demo.auth.domain.UserInfo;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.*;



import java.time.LocalDateTime;


@Entity
@Table(name="review_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewComment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id",nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private UserInfo user;

    private String content;

    @Column(name = "is_resident")
    private boolean isResident;  //매핑시 getIsxxx가 아닌 isXXX로 매핑됨

    @Column(name = "has_children")
    private boolean hasChildren;   //매핑시 getHasxxx가 아닌 HasXXX로 매핑됨



    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    //본인 확인
    public boolean isMine(UserInfo currentUser){
        return user != null && user.getUserId().equals(currentUser.getUserId());
    }

    //댓글 수정: 내용
    public void updateContent(String content){
        this.content = content;
        onUpdate();
    }


    // 댓글 삭제
    public void deleteReviewComment() {
        this.deletedAt = LocalDateTime.now();
    }

}
