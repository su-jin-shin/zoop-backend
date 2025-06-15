package com.example.demo.auth.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_info")
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE user_info SET deleted_at = NOW() WHERE user_id = ?")
@Where(clause = "deleted_at IS NULL")      // 조회 시 자동 필터
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

    /* --- 탈퇴 전용 도메인 메서드 --- */
    public void withdraw(String reason) {
        if (this.deletedAt != null) {
            throw new IllegalStateException("이미 탈퇴 처리된 회원입니다.");
        }
        this.withdrawReason = reason;
        this.deletedAt = LocalDateTime.now();
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
