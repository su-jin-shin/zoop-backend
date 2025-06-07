package com.example.demo.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMembers is a Querydsl query type for Members
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMembers extends EntityPathBase<Members> {

    private static final long serialVersionUID = 237538283L;

    public static final QMembers members = new QMembers("members");

    public final ListPath<ReviewCommentLike, QReviewCommentLike> commentLikes = this.<ReviewCommentLike, QReviewCommentLike>createList("commentLikes", ReviewCommentLike.class, QReviewCommentLike.class, PathInits.DIRECT2);

    public final ListPath<ReviewComment, QReviewComment> comments = this.<ReviewComment, QReviewComment>createList("comments", ReviewComment.class, QReviewComment.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> created_at = createDateTime("created_at", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deleted_at = createDateTime("deleted_at", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath profile_image = createString("profile_image");

    public final ListPath<ReviewLike, QReviewLike> reviewLikes = this.<ReviewLike, QReviewLike>createList("reviewLikes", ReviewLike.class, QReviewLike.class, PathInits.DIRECT2);

    public final ListPath<Review, QReview> reviews = this.<Review, QReview>createList("reviews", Review.class, QReview.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> updated_at = createDateTime("updated_at", java.time.LocalDateTime.class);

    public QMembers(String variable) {
        super(Members.class, forVariable(variable));
    }

    public QMembers(Path<? extends Members> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMembers(PathMetadata metadata) {
        super(Members.class, metadata);
    }

}

