package com.example.demo.property.repository;

import com.example.demo.property.domain.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Long> {
}
