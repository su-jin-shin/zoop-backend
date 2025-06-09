package com.example.demo.auth.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Entity
@Table(name = "user_info")
@Getter
@NoArgsConstructor
@SuppressFBWarnings(value = "UWF_UNWRITTEN_FIELD",
        justification = "withdrawReason과 deletedAt은 미래에 사용될 가능성이 있어 SpotBugs 무시")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private Long kakaoId;

    private String email;

    @Column(nullable = true, unique = true)
    private String nickname;
    private String profileImage;
    private String withdrawReason;

    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<LoginHistory> loginHistories = new ArrayList<>();

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
