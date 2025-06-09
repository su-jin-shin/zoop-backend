package com.example.demo.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -1511484858L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final ListPath<ReviewComment, QReviewComment> comments = this.<ReviewComment, QReviewComment>createList("comments", ReviewComment.class, QReviewComment.class, PathInits.DIRECT2);

    public final QComplex complex;

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> created_at = createDateTime("created_at", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deleted_at = createDateTime("deleted_at", java.time.LocalDateTime.class);

    public final BooleanPath has_children = createBoolean("has_children");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath is_resident = createBoolean("is_resident");

    public final NumberPath<Long> like_count = createNumber("like_count", Long.class);

    public final QMembers members;

    public final DateTimePath<java.time.LocalDateTime> updated_at = createDateTime("updated_at", java.time.LocalDateTime.class);

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.complex = inits.isInitialized("complex") ? new QComplex(forProperty("complex"), inits.get("complex")) : null;
        this.members = inits.isInitialized("members") ? new QMembers(forProperty("members")) : null;
    }

}

