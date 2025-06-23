package com.example.demo.review.repository;

import com.example.demo.review.domain.Complex;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public interface ComplexRepository extends JpaRepository<Complex, Long> {

    /**
     * 매물 ID를 통해 해당 매물이 속한 단지(Complex) 정보를 조회합니다.
     *
     * - 특정 매물의 단지 정보가 필요한 경우 (예: 리뷰 단지 단위 매핑)
     *
     * @param propertyId 조회할 매물 ID
     * @return 해당 매물이 속한 단지가 존재하면 Optional<Complex>로 반환
     */

    @Query(value = "SELECT c.* FROM complex c " +
            "JOIN property p ON c.complex_id = p.complex_id " +
            "WHERE p.property_id = :propertyId", nativeQuery = true)
    Optional<Complex> findByPropertyId(@Param("propertyId") Long propertyId);


}
