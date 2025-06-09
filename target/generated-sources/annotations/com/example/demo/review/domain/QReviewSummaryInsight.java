package com.example.demo.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewSummaryInsight is a Querydsl query type for ReviewSummaryInsight
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewSummaryInsight extends EntityPathBase<ReviewSummaryInsight> {

    private static final long serialVersionUID = 1424707832L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewSummaryInsight reviewSummaryInsight = new QReviewSummaryInsight("reviewSummaryInsight");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReviewSummary reviewSummary;

    public final EnumPath<InsightType> type = createEnum("type", InsightType.class);

    public QReviewSummaryInsight(String variable) {
        this(ReviewSummaryInsight.class, forVariable(variable), INITS);
    }

    public QReviewSummaryInsight(Path<? extends ReviewSummaryInsight> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewSummaryInsight(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewSummaryInsight(PathMetadata metadata, PathInits inits) {
        this(ReviewSummaryInsight.class, metadata, inits);
    }

    public QReviewSummaryInsight(Class<? extends ReviewSummaryInsight> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reviewSummary = inits.isInitialized("reviewSummary") ? new QReviewSummary(forProperty("reviewSummary"), inits.get("reviewSummary")) : null;
    }

}

