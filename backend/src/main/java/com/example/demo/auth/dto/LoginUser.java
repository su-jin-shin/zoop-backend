package com.example.demo.auth.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@SuppressFBWarnings(
        value = {"EI_EXPOSE_REP2", "SE_BAD_FIELD"},
        justification = "세션 DTO 용도"
)
public class LoginUser implements UserDetails {

    private final Long userId;

    public LoginUser(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() { return userId; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(); }
    @Override public String getPassword() { return ""; }
    @Override public String getUsername() { return String.valueOf(userId); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
