package com.example.demo.Filter.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QKeywordFilterHistory is a Querydsl query type for KeywordFilterHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKeywordFilterHistory extends EntityPathBase<KeywordFilterHistory> {

    private static final long serialVersionUID = 130601281L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QKeywordFilterHistory keywordFilterHistory = new QKeywordFilterHistory("keywordFilterHistory");

    public final QFilter filter;

    public final BooleanPath isUsed = createBoolean("isUsed");

    public final NumberPath<Long> keywordFilterHistoryId = createNumber("keywordFilterHistoryId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> usedAt = createDateTime("usedAt", java.time.LocalDateTime.class);

    public final com.example.demo.auth.domain.QUserInfo userInfo;

    public QKeywordFilterHistory(String variable) {
        this(KeywordFilterHistory.class, forVariable(variable), INITS);
    }

    public QKeywordFilterHistory(Path<? extends KeywordFilterHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QKeywordFilterHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QKeywordFilterHistory(PathMetadata metadata, PathInits inits) {
        this(KeywordFilterHistory.class, metadata, inits);
    }

    public QKeywordFilterHistory(Class<? extends KeywordFilterHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.filter = inits.isInitialized("filter") ? new QFilter(forProperty("filter")) : null;
        this.userInfo = inits.isInitialized("userInfo") ? new com.example.demo.auth.domain.QUserInfo(forProperty("userInfo")) : null;
    }

}

