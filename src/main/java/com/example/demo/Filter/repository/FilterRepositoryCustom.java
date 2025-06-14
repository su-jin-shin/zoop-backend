package com.example.demo.Filter.repository;

import com.example.demo.Filter.domain.Filter;

import java.util.Optional;

public interface FilterRepositoryCustom {
    Optional<Filter> findDuplicate(Filter filter);
}
