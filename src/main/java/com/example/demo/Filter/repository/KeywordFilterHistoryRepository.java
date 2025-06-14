package com.example.demo.Filter.repository;

import com.example.demo.Filter.domain.KeywordFilterHistory;
import com.example.demo.auth.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KeywordFilterHistoryRepository extends JpaRepository<KeywordFilterHistory, Long> {

    Optional<KeywordFilterHistory> findByKeywordFilterHistoryIdAndUserInfo(Long keywordFilterHistoryId, UserInfo userInfo);

    // 키워드 설정 수정시 기존 필터 비활성과 현재 필터를 키워드필터히스토리에 기록
    @Modifying
    @Query("UPDATE KeywordFilterHistory h " +
            "SET h.isUsed = false " +
            "WHERE h.keywordFilterHistoryId = :keywordFilterHistoryId")
    void disableKeywordFilterHistory(@Param("keywordFilterHistoryId") Long keywordFilterHistoryId);

    List<KeywordFilterHistory> findByUserInfoAndIsUsedTrue(UserInfo userInfo);
}
