package com.example.demo.realty.repository;

import com.example.demo.property.domain.Realty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealtyRepository extends JpaRepository<Realty , Long> {
}
