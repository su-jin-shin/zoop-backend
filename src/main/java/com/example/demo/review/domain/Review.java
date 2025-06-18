package com.example.demo.review.domain;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.dto.LoginUser;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;


import java.math.BigDecimal;
import java.time.LocalDateTime;



@Entity
@Table(name="review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Builder.Default
    private Long likeCount = 0L;

    @Enumerated(EnumType.STRING)
    @Column(name = "has_children")
    @Builder.Default
    private HasChildren hasChildren = HasChildren.NO_CHILDREN;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_resident")
    @Builder.Default
    private IsResident isResident = IsResident.NON_RESIDENT;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /* 비즈니스 메서드 */



    public boolean isMine(UserInfo currentUser) {
        return user != null && user.getUserId().equals(currentUser.getUserId());
    }


    //리뷰 수정: 내용(content), 별점(rating)
    public void updateContent(String content){
        this.content = content;
        onUpdate();
    }
    public void updateRating(BigDecimal rating){
        this.rating = rating;
        onUpdate();
    }

    // 리뷰 삭제
    public void deleteReview() {
        this.deletedAt = LocalDateTime.now();
    }


}
