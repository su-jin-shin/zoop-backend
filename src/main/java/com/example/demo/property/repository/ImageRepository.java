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

    //썸네일 이미지 가져오기
    @Query(value = """
    SELECT DISTINCT ON (i.property_id) *
    FROM image i
    WHERE i.property_id IN (:propertyIds)
      AND i.deleted_at IS NULL
      AND i.image_type = 'PROPERTY'
    ORDER BY i.property_id,
             CASE WHEN i.is_main THEN 0 ELSE 1 END,
             i.image_order ASC
    """, nativeQuery = true)
    List<Image> findThumbnailsByPropertyIds(@Param("propertyIds") List<Long> propertyIds);

    List<Image> findByProperty_PropertyIdIn(List<Long> propertyIds);
}
