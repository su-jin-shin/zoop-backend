package com.example.demo.auth.repository;

import com.example.demo.auth.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Boolean existsByEmail(String email);
    Optional<UserInfo> findByEmail(String email);
    Optional<UserInfo> findByUserId(Long userId);
    Optional<UserInfo> findByEmailAndDeletedAtIsNull(String email);

}
