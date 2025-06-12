package com.example.demo.mypage.repository;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.mypage.domain.RecentViewedProperty;
import com.example.demo.property.domain.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecentViewedPropertyRepository extends JpaRepository<RecentViewedProperty, Long> {

    Optional<RecentViewedProperty> findByUserAndProperty(UserInfo user, Property property);
    List<RecentViewedProperty> findTop20ByUser_UserIdOrderByViewedAtDesc(Long userId);
}
