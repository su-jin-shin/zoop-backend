package com.example.demo.Filter.repository;

import com.example.demo.Filter.domain.Filter;
import com.example.demo.Filter.domain.Region;
import com.example.demo.auth.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    // 지역번호가 있으면 region 반환
    Optional<Region> findByCortarNo(String cortarNo);

}
