package com.example.demo.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QComplex is a Querydsl query type for Complex
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QComplex extends EntityPathBase<Complex> {

    private static final long serialVersionUID = 239150978L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QComplex complex = new QComplex("complex");

    public final StringPath complex_name = createString("complex_name");

    public final NumberPath<Long> complex_no = createNumber("complex_no", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Review, QReview> reviews = this.<Review, QReview>createList("reviews", Review.class, QReview.class, PathInits.DIRECT2);

    public final QReviewSummary reviewSummary;

    public QComplex(String variable) {
        this(Complex.class, forVariable(variable), INITS);
    }

    public QComplex(Path<? extends Complex> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QComplex(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QComplex(PathMetadata metadata, PathInits inits) {
        this(Complex.class, metadata, inits);
    }

    public QComplex(Class<? extends Complex> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reviewSummary = inits.isInitialized("reviewSummary") ? new QReviewSummary(forProperty("reviewSummary"), inits.get("reviewSummary")) : null;
    }

}

