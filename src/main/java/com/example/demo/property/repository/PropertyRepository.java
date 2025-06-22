package com.example.demo.property.repository;

import com.example.demo.property.domain.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Long> {
    //비페이징 메서드
    List<Property> findByRealty_RealtyId(Long realtyId);

    //페이징 지원 메서드
    Page<Property> findByRealty_RealtyId(Long realtyId, Pageable pageable);

    //거래 유형
    Page<Property> findByRealty_RealtyIdAndTradeTypeName(Long realtyId, String tradeTypeName,Pageable pageable);

    //ai
    List<Property> findByDeletedAtIsNull();

}
