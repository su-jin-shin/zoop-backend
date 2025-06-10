package com.example.demo.property.repository;

import com.example.demo.property.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

    //매물 ID에 해당하는 이미지 리스트 조회
    List<Image> findByProperty_PropertyId(Long propertyId);

}
