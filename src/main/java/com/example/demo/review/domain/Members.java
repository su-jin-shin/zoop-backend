//package com.example.demo.review.domain;
//
//
//import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "members")
//@Getter
//@Setter
//@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
//public class Members {
//
//    @Id
//    @GeneratedValue
//    @Column(name = "members_id")
//    private Long id;
//
//    private String nickname;
//
//    private String profile_image;
//
//    private LocalDateTime created_at;
//    private LocalDateTime updated_at;
//    private LocalDateTime deleted_at;
//
//    // 테이블과 연관  / 필드 초기화
//    @OneToMany(mappedBy = "members")
//    private List<Review> reviews = new ArrayList<>();
//
//    @OneToMany(mappedBy = "members")
//    private List<ReviewComment> comments = new ArrayList<>();
//
//    @OneToMany(mappedBy = "members")
//    private List<ReviewLike> reviewLikes =  new ArrayList<>();
//
//
//    @OneToMany(mappedBy = "members")
//    private List<ReviewCommentLike> commentLikes = new ArrayList<>();
//
//
//}
