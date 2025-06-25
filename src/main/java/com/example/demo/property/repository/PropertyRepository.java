package com.example.demo.property.repository;

import com.example.demo.property.domain.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    //매물 번호를 통하여 매물 조회
    Optional<Property> findByArticleNo(String articleNo);

    /* ai_summary 컬럼만 업데이트하는 native 쿼리 */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE "property" 
        SET ai_summary = CAST(:aiSummary AS jsonb)
        WHERE property_id = :propertyId
        """,
            nativeQuery = true)
    int updateAiSummaryNative(@Param("propertyId") Long propertyId,
                              @Param("aiSummary") String aiSummary);

}
