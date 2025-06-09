package com.example.demo.mypage.repository;

import com.example.demo.auth.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MypageUserInfoRepository extends JpaRepository<UserInfo, Long> {

}
