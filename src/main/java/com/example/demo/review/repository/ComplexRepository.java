package com.example.demo.review.repository;

import com.example.demo.review.domain.Complex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComplexRepository extends JpaRepository<Complex, Long> {


    @Query(value = "SELECT c.* FROM complex c " +
            "JOIN property p ON c.complex_id = p.complex_id " +
            "WHERE p.property_id = :propertyId", nativeQuery = true)
    Optional<Complex> findByPropertyId(@Param("propertyId") Long propertyId);


}
