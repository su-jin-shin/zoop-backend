package com.example.demo.realty.repository;

import com.example.demo.realty.domain.Realty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealtyRepository extends JpaRepository<Realty , Long> {
}
