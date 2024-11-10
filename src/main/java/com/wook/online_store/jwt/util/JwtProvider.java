package com.wook.online_store.jwt.util;

import com.wook.online_store.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;
    private final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60;
    private final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 7;

    public String generateAccessToken(String username, List<Role> roles) {
        return generateToken(username, roles, ACCESS_TOKEN_VALIDITY);
    }

    // 리프레시 토큰은 roles를 넘겨주지 않아도 됨
    public String generateRefreshToken(String username) {
        return generateToken(username, null, REFRESH_TOKEN_VALIDITY);
    }

    // 리프레시 토큰을 사용해서 새로운 엑세스 토큰 발급
    public String refreshAccessToken(String refreshToken, String username, List<Role> roles) {
        if (validateToken(refreshToken, username)) {
            return generateAccessToken(username, roles);
        }
        throw new JwtException("Invalid refresh token or token expired");
    }

    public String generateToken(String username, List<Role> roles, long validity) {
        Claims claims = Jwts.claims();
        claims.put("sub", username);
        if (roles != null) {
            claims.put("roles", roles.stream()
                    .map(Role::name)
                    .collect(Collectors.toList())); // List<String>형태로 저장하여 호환성, 성능을 올림
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(getSignKey())
                .compact();
    }

    // 토큰에서 사용자명 추출
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // JWT토큰에서 사용자 정보 추출
    public Claims extractClaims(String token) {
        return  Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody(); // 클레임 반환
    }

    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    /**
     * HMAC (Hash-based Message Authentication Code) SHA
     * Base64로 디코딩
     * HMAC SHA 알고리즘을 사용
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
