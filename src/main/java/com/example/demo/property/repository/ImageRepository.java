package com.example.demo.property.repository;

import com.example.demo.property.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

    //매물 ID에 해당하는 이미지 리스트 조회
    List<Image> findByProperty_PropertyId(Long propertyId);

    //제일 처음 이미지 가져오기
    @Query("""
    SELECT i FROM Image i
    WHERE i.property.propertyId IN :propertyIds
      AND i.isMain = true
      AND i.deletedAt IS NULL
    ORDER BY i.imageOrder ASC
""")
    List<Image> findTopImagesByPropertyIds(@Param("propertyIds") List<Long> propertyIds);
}
