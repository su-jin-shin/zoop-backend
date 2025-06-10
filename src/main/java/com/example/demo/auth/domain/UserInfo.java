package com.example.demo.auth.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import java.time.LocalDateTime;

@Entity
@Table(name = "user_info")
@Getter
@NoArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private Long kakaoId;

    private String email;

    @Column(unique = true)
    private String nickname;

    @Column(columnDefinition = "TEXT")
    private String profileImage;

    private String withdrawReason;

    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<LoginHistory> loginHistories = new ArrayList<>();

    /* --- 탈퇴 전용 도메인 메서드 --- */
    public void withdraw(String reason) {
        if (this.deletedAt != null) {           // 이미 탈퇴 처리된 계정이면 예외 던져도 OK
            throw new IllegalStateException("이미 탈퇴 처리된 회원입니다.");
        }
        this.withdrawReason = reason;
        this.deletedAt = LocalDateTime.now();   // 소프트 삭제 ― deleted_at 세팅
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
    public void setKakaoId(Long kakaoId) {
        this.kakaoId = kakaoId;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /* ---------- 방어적 복사 getter/setter ---------- */
    public List<LoginHistory> getLoginHistories() {
        return Collections.unmodifiableList(loginHistories);
    }
    public void setLoginHistories(List<LoginHistory> histories) {
        this.loginHistories = new ArrayList<>(histories);
    }





}
