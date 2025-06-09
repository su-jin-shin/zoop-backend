package com.example.demo.auth.dto;

import com.example.demo.auth.domain.UserInfo;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@SuppressFBWarnings(
        value = {"EI_EXPOSE_REP2", "SE_BAD_FIELD"},
        justification = "세션 DTO 용도"
)
public class LoginUser implements UserDetails {

    private final UserInfo userInfo;

    public LoginUser(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    // 편의 게터
    public Long   getUserId()      { return userInfo.getUserId();     }
    public String getEmail()       { return userInfo.getEmail();      }
    public String getNickname()    { return userInfo.getNickname();   }
    public String getProfileImage(){ return userInfo.getProfileImage();}

    // UserDetails 구현
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(); }
    @Override public String getPassword()          { return ""; }
    @Override public String getUsername()          { return String.valueOf(userInfo.getUserId()); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked()  { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()           { return true; }
}
