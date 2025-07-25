package com.example.demo.review.dto.Review.Response;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

import java.util.List;

// 리뷰 요약 응답
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class AiSummaryResponse {
    private List<String> good;
    private List<String> bad;
    private List<String> tra;
    private List<String> edu;
    private List<String> hel;
    private List<String> loc;
}

