package com.example.demo.auth.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long ACCESS_EXP  = 30L * 24 * 60 * 60 * 1000;        // 30 d
    private static final long REFRESH_EXP = 90L * 24 * 60 * 60 * 1000; // 90 d

    /* ---------------- 헬퍼 ---------------- */
    private byte[] keyBytes() {
        // DM_DEFAULT_ENCODING --> UTF-8 명시
        return secretKey.getBytes(StandardCharsets.UTF_8);
    }

    /* ---------------- 발급 ---------------- */
    public String generateAccess(String sub)  { return build(sub, ACCESS_EXP); }
    public String generateRefresh(String sub) { return build(sub, REFRESH_EXP); }

    /* ---------------- 검증 ---------------- */
    public boolean isExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(keyBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public String getSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(keyBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /* ---------------- 내부 ---------------- */
    private String build(String sub, long exp) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(sub)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + exp))
                .signWith(Keys.hmacShaKeyFor(keyBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
