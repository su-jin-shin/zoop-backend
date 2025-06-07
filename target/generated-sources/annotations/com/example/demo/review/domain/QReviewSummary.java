package com.example.demo.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewSummary is a Querydsl query type for ReviewSummary
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewSummary extends EntityPathBase<ReviewSummary> {

    private static final long serialVersionUID = 750826112L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewSummary reviewSummary = new QReviewSummary("reviewSummary");

    public final ListPath<ReviewSummaryCategory, QReviewSummaryCategory> categories = this.<ReviewSummaryCategory, QReviewSummaryCategory>createList("categories", ReviewSummaryCategory.class, QReviewSummaryCategory.class, PathInits.DIRECT2);

    public final QComplex complex;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<ReviewSummaryInsight, QReviewSummaryInsight> insights = this.<ReviewSummaryInsight, QReviewSummaryInsight>createList("insights", ReviewSummaryInsight.class, QReviewSummaryInsight.class, PathInits.DIRECT2);

    public final NumberPath<Float> overall_rating = createNumber("overall_rating", Float.class);

    public QReviewSummary(String variable) {
        this(ReviewSummary.class, forVariable(variable), INITS);
    }

    public QReviewSummary(Path<? extends ReviewSummary> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewSummary(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewSummary(PathMetadata metadata, PathInits inits) {
        this(ReviewSummary.class, metadata, inits);
    }

    public QReviewSummary(Class<? extends ReviewSummary> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.complex = inits.isInitialized("complex") ? new QComplex(forProperty("complex"), inits.get("complex")) : null;
    }

}

