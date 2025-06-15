package com.example.demo.Filter.repository;

import com.example.demo.Filter.domain.Filter;
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

    // 로그인한 사용자의 사용중인 키워드 필터 히스토리 목록 전체 조회
    List<KeywordFilterHistory> findByUserInfoAndIsUsedTrue(UserInfo userInfo);

    // 로그인한 사용자가 필터와 키워드 필터가 true이면 true 반환
    boolean existsByUserInfoAndFilterAndIsUsedTrue(UserInfo userInfo, Filter filter);
}
