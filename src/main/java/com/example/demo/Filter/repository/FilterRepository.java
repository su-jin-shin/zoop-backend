package com.example.demo.Filter.repository;

import com.example.demo.Filter.domain.Filter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterRepository extends JpaRepository<Filter, Long> , FilterRepositoryCustom{
}
