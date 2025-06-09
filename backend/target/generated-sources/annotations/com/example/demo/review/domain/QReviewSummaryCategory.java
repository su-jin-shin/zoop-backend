package com.example.demo.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewSummaryCategory is a Querydsl query type for ReviewSummaryCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewSummaryCategory extends EntityPathBase<ReviewSummaryCategory> {

    private static final long serialVersionUID = 721638302L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewSummaryCategory reviewSummaryCategory = new QReviewSummaryCategory("reviewSummaryCategory");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Float> rating = createNumber("rating", Float.class);

    public final QReviewSummary reviewSummary;

    public final EnumPath<CategoryType> type = createEnum("type", CategoryType.class);

    public QReviewSummaryCategory(String variable) {
        this(ReviewSummaryCategory.class, forVariable(variable), INITS);
    }

    public QReviewSummaryCategory(Path<? extends ReviewSummaryCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewSummaryCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewSummaryCategory(PathMetadata metadata, PathInits inits) {
        this(ReviewSummaryCategory.class, metadata, inits);
    }

    public QReviewSummaryCategory(Class<? extends ReviewSummaryCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reviewSummary = inits.isInitialized("reviewSummary") ? new QReviewSummary(forProperty("reviewSummary"), inits.get("reviewSummary")) : null;
    }

}

