package com.example.demo.auth.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}") private String secretKey;
    private static final long ACCESS_EXP  = 60 * 60 * 1000;        // 1일
    private static final long REFRESH_EXP = 14L * 24 * 60 * 60 * 1000; // 14일

    public String generateAccess(String sub){ return build(sub, ACCESS_EXP); }
    public String generateRefresh(String sub){ return build(sub, REFRESH_EXP); }

    public Boolean isExpired(String token){
        return Jwts.parserBuilder().setSigningKey(secretKey.getBytes())
                .build().parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());
    }
    public String getSubject(String token){
        return Jwts.parserBuilder().setSigningKey(secretKey.getBytes())
                .build().parseClaimsJws(token)
                .getBody().getSubject();
    }
    private String build(String sub,long exp){
        Date now = new Date();
        return Jwts.builder()
                .setSubject(sub)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+exp))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()),SignatureAlgorithm.HS256)
                .compact();
    }
}
