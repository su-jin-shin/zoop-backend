package com.example.demo.Filter.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFilter is a Querydsl query type for Filter
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFilter extends EntityPathBase<Filter> {

    private static final long serialVersionUID = 13350566L;

    public static final QFilter filter = new QFilter("filter");

    public final StringPath cortarNo = createString("cortarNo");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath dealOrWarrantPrc = createString("dealOrWarrantPrc");

    public final NumberPath<Long> filterId = createNumber("filterId", Long.class);

    public final StringPath filterTitle = createString("filterTitle");

    public final EnumPath<com.example.demo.Filter.domain.enums.RealEstateTypeName> realEstateTypeName = createEnum("realEstateTypeName", com.example.demo.Filter.domain.enums.RealEstateTypeName.class);

    public final NumberPath<java.math.BigDecimal> rentPrice = createNumber("rentPrice", java.math.BigDecimal.class);

    public final EnumPath<com.example.demo.Filter.domain.enums.TradeTypeName> tradeTypeName = createEnum("tradeTypeName", com.example.demo.Filter.domain.enums.TradeTypeName.class);

    public QFilter(String variable) {
        super(Filter.class, forVariable(variable));
    }

    public QFilter(Path<? extends Filter> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFilter(PathMetadata metadata) {
        super(Filter.class, metadata);
    }

}

