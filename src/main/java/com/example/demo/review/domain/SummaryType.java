package com.example.demo.review.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public enum SummaryType {
    GOOD, //장점
    BAD,  //단점
    TRA,  // 교통
    EDU,  // 교육
    HEL,  // 의료
    LOC   // 위치
}
