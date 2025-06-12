package com.example.demo.mypage.repository;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.mypage.domain.BookmarkedProperty;
import com.example.demo.property.domain.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookmarkedPropertyRepository extends JpaRepository<BookmarkedProperty, Long> {

    Optional<BookmarkedProperty> findByUserAndProperty(UserInfo user, Property property);

    boolean existsByUser_UserIdAndProperty_PropertyIdAndIsBookmarkedTrue(Long userId, Long propertyId);

    @Query("""
    SELECT bp.property.propertyId
    FROM BookmarkedProperty bp
    WHERE bp.user.userId = :userId
      AND bp.property.propertyId IN :propertyIds
      AND bp.isBookmarked = true
""")
    Set<Long> findBookmarkedPropertyIds(@Param("userId") Long userId,
                                        @Param("propertyIds") List<Long> propertyIds);

}
