package com.example.demo.Filter.repository.impl;


import com.example.demo.Filter.domain.Filter;
import com.example.demo.Filter.domain.QFilter;
import com.example.demo.Filter.repository.FilterRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@SuppressFBWarnings(value = "EI_EXPOSE_REP2")
@Repository
@RequiredArgsConstructor
public class FilterRepositoryImpl implements FilterRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Filter> findDuplicate(Filter filter) {
        QFilter q = QFilter.filter;

        return Optional.ofNullable(
                queryFactory.selectFrom(q)
                        .where(
                                q.region.eq(filter.getRegion()),
                                q.x.eq(filter.getX()),
                                q.y.eq(filter.getY()),
                                q.bCode.eq(filter.getBCode()),
                                q.hCode.eq(filter.getHCode()),
                                q.tradeTypeName.eq(filter.getTradeTypeName()),
                                q.realEstateTypeName.eq(filter.getRealEstateTypeName()),
                                q.dealOrWarrantPrc.eq(filter.getDealOrWarrantPrc()),
                                q.rentPrice.eq(filter.getRentPrice())
                        )
                        .fetchFirst()
        );
    }
}
