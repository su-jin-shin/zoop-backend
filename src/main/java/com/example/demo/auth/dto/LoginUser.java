package com.example.demo.auth.dto;

import com.example.demo.auth.domain.UserInfo;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
@SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "LoginUser는 세션 DTO 용도이며, UserInfo는 JPA Entity로 변경 위험이 낮음"
)
public class LoginUser implements UserDetails {

    @Getter(lombok.AccessLevel.NONE)
    @SuppressFBWarnings(
            value = "SE_BAD_FIELD",
            justification = "LoginUser는 userInfo 직렬화를 요구하지 않으며, 세션 저장 시 해당 필드는 무시해도 무방합니다."
    )
    private final UserInfo userInfo;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return userInfo.getUserId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}