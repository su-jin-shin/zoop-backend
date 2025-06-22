package com.example.demo.Filter.repository.impl;


import com.example.demo.Filter.domain.Filter;
import com.example.demo.Filter.domain.QFilter;
import com.example.demo.Filter.repository.FilterRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@SuppressFBWarnings(value = "EI_EXPOSE_REP2")
@Repository
@RequiredArgsConstructor
public class FilterRepositoryImpl implements FilterRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Filter> findDuplicate(Filter filter) {
        QFilter q = QFilter.filter;

        // 1차 필터 데이터와 비교
        List<Filter> candidates = queryFactory.selectFrom(q)
                .where(
                        q.region.eq(filter.getRegion()),
                        q.longitude.eq(filter.getLongitude()),
                        q.latitude.eq(filter.getLatitude()),
                        q.bCode.eq(filter.getBCode()),
                        q.hCode.eq(filter.getHCode()),
                        q.tradeTypeName.eq(filter.getTradeTypeName()),
                        q.dealOrWarrantPrc.eq(filter.getDealOrWarrantPrc()),
                        q.rentPrice.eq(filter.getRentPrice())
                )
                .fetch();

        // 2차 매물 타입 순서 상관없이 비교
        return candidates.stream()
                .filter(existing -> {
                    Set<String> existingTypes = Optional.ofNullable(existing.getRealEstateTypeName()).orElse(Collections.emptyList()).stream().collect(Collectors.toSet());
                    Set<String> inputTypes = Optional.ofNullable(filter.getRealEstateTypeName()).orElse(Collections.emptyList()).stream().collect(Collectors.toSet());
                    return existingTypes.equals(inputTypes);
                })
                .findFirst();
    }
}
