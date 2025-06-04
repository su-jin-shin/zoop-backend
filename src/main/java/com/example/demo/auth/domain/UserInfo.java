package com.example.demo.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private Long kakaoId;

    private String email;
    private String nickname;
    private String profileImage;
    private String withdrawReason;

    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    // JPA 생명주기 이벤트로 시간 자동 설정
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.lastLoginAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<LoginHistory> loginHistories = new ArrayList<>();

}
