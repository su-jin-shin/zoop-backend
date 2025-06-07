package com.example.demo.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewCommentLike is a Querydsl query type for ReviewCommentLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewCommentLike extends EntityPathBase<ReviewCommentLike> {

    private static final long serialVersionUID = 1690490992L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewCommentLike reviewCommentLike = new QReviewCommentLike("reviewCommentLike");

    public final DateTimePath<java.time.LocalDateTime> created_at = createDateTime("created_at", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deleted_at = createDateTime("deleted_at", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath is_liked = createBoolean("is_liked");

    public final QMembers members;

    public final QReviewComment reviewComment;

    public final DateTimePath<java.time.LocalDateTime> updated_at = createDateTime("updated_at", java.time.LocalDateTime.class);

    public QReviewCommentLike(String variable) {
        this(ReviewCommentLike.class, forVariable(variable), INITS);
    }

    public QReviewCommentLike(Path<? extends ReviewCommentLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewCommentLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewCommentLike(PathMetadata metadata, PathInits inits) {
        this(ReviewCommentLike.class, metadata, inits);
    }

    public QReviewCommentLike(Class<? extends ReviewCommentLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.members = inits.isInitialized("members") ? new QMembers(forProperty("members")) : null;
        this.reviewComment = inits.isInitialized("reviewComment") ? new QReviewComment(forProperty("reviewComment"), inits.get("reviewComment")) : null;
    }

}

