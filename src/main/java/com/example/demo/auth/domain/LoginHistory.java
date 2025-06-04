package com.example.demo.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_history")
@Getter
@Setter
@NoArgsConstructor
public class LoginHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loginHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)   // 외래키 컬럼 명시
    private UserInfo user;

    private LocalDateTime loginAt;
    private String ipAddress;
}
