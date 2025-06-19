package com.example.demo.review.dto.Review;

import com.example.demo.review.domain.HasChildren;
import com.example.demo.review.domain.IsResident;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//개별 리뷰 정보 DTO
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCreateResponse {
    private Long reviewId;
    private Long userId;
    private String nickname;
    private String profileImage;
    private BigDecimal rating;
    private String content;
    private HasChildren hasChildren;
    private IsResident isResident;
    private Long likeCount;
    private Long commentCount;
    private Boolean isLikedByMe;
    private Boolean isMine;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
