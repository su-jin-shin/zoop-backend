package com.example.demo.config;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;


public class QueryTest {
    private final JPAQueryFactory queryFactory;

    public QueryTest(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
}
