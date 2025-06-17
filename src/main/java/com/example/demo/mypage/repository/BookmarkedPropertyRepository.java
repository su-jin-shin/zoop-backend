package com.example.demo.mypage.repository;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.mypage.domain.BookmarkedProperty;
import com.example.demo.mypage.dto.BookmarkedPropertyPageResponse;
import com.example.demo.property.domain.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookmarkedPropertyRepository extends JpaRepository<BookmarkedProperty, Long>, BookmarkedPropertyQueryRepository {


    @Query("""
    SELECT bp.property.propertyId
    FROM BookmarkedProperty bp
    WHERE bp.user.userId = :userId
      AND bp.property.propertyId IN :propertyIds
      AND bp.isBookmarked = true
    """)
    Set<Long> findBookmarkedPropertyIds(@Param("userId") Long userId,
                                        @Param("propertyIds") List<Long> propertyIds);

    @Query(value = """
    SELECT bp FROM BookmarkedProperty bp
    JOIN FETCH bp.property
    WHERE bp.user.userId = :userId AND bp.isBookmarked = true
    """,
            countQuery = """
    SELECT COUNT(bp) FROM BookmarkedProperty bp
    WHERE bp.user.userId = :userId AND bp.isBookmarked = true
    """
    )
    Page<BookmarkedProperty> findAllWithPropertyByUserId(@Param("userId") Long userId, Pageable pageable);

    Optional<BookmarkedProperty> findByUserAndProperty(UserInfo userInfo, Property property);

    @Query("""
    SELECT bp FROM BookmarkedProperty bp
    JOIN FETCH bp.property p
    LEFT JOIN FETCH p.realty
    WHERE bp.user.userId = :userId AND bp.isBookmarked = true
    ORDER BY bp.createdAt DESC
    """)
    List<BookmarkedProperty> findAllWithPropertyAndRealtyByUserId(@Param("userId") Long userId);

    boolean existsByUser_UserIdAndProperty_PropertyIdAndIsBookmarkedTrue(Long userId, Long propertyId);

}
