package com.example.demo.auth.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

@Entity
@Table(name = "user_info")
@Getter
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long userId;


    private Long kakaoId;       // ← NOT NULL 제약 제거

    private String email;


    private String nickname;

    @Column(columnDefinition = "TEXT")
    private String profileImage;

    private String withdrawReason;

    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    /* --- 탈퇴 --- */
    public void withdraw(String reason) {
        if (this.deletedAt != null) throw new IllegalStateException("이미 탈퇴 처리된 회원입니다.");

        // 식별 정보 제거(또는 익명 처리)
        this.email        = null;                    // 또는 "deleted_"+userId+"@anon"
        this.kakaoId      = null;
        this.nickname     = null;
        this.profileImage = null;

        this.withdrawReason = reason;
        this.deletedAt      = LocalDateTime.now();
    }

    /* --- 복구(재가입) --- */
    public void reactivate() {
        if (this.deletedAt == null) return;     // 이미 활성 상태면 무시
        this.deletedAt = null;
        this.withdrawReason = null;
        this.profileImage = null;
        this.lastLoginAt = LocalDateTime.now();
    }

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.lastLoginAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /* ---------- 필요한 Setter들만 수동으로 ---------- */
    public void setKakaoId(Long kakaoId)       { this.kakaoId   = kakaoId; }
    public void setEmail(String email)         { this.email     = email; }
    public void setProfileImage(String img)    { this.profileImage = img; }
    public void setLastLoginAt(LocalDateTime t){ this.lastLoginAt = t; }
    public void setNickname(String nickname)   { this.nickname  = nickname; }
}
