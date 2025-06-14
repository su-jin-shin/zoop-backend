package com.example.demo.Filter.repository;

import com.example.demo.Filter.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByCortarNo(String hCode);
}
