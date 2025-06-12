package com.example.demo.mypage.domain;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.property.domain.Property;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookmarked_property", uniqueConstraints = {
        @UniqueConstraint(name = "uniq_user_bookmark", columnNames = {"user_id", "property_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkedProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkedPropertyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "is_bookmarked", nullable = false)
    private Boolean isBookmarked;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
