package com.wook.online_store.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JWTUtil {

    private final byte[] accessSecret;
    private final byte[] refreshSecret;

    private final static Long ACCESS_TOKEN_EXPIRE_COUNT = 30 * 30 * 1000L; // 30분
    private final static Long REFRESH_TOKEN_EXPIRE_COUNT = 7 * 24 * 60 * 60 * 1000L; // 7일

    // 키 생성
    public JWTUtil(@Value("${jwt.secretKey}")String accessSecret, @Value("jwt.refreshKey") String refreshSecret) {
        this.accessSecret = accessSecret.getBytes(StandardCharsets.UTF_8);
        this.refreshSecret = refreshSecret.getBytes(StandardCharsets.UTF_8);
    }

    public String createAccessToken(Long id, String email, String name, List<String> roles) {
        return createJwt(id, email, name, roles, ACCESS_TOKEN_EXPIRE_COUNT, accessSecret);
    }

    public String createRefreshToken(Long id, String email, String name, List<String> roles) {
        return createJwt(id, email, name, roles, REFRESH_TOKEN_EXPIRE_COUNT, refreshSecret);
    }

    public String createJwt(Long id, String email, String name, List<String> roles, Long expire, byte[] secretKey) {

        Claims claims = Jwts.claims().setSubject(email);

        claims.put("roles", roles);
        claims.put("userId", id);
        claims.put("name", name);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expire))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    // 토큰에서 유저 아이디 얻기
    public Long getUserIdFromToken(String token) {
        String[] tokenArr = token.split(" ");
        token = tokenArr[1];
        Claims claims = parseToken(token, accessSecret);

        return Long.valueOf((Integer) claims.get("userId"));
    }

    public Claims parseToken(String token, byte[] secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims parseAccessToken(String accessToken) {
        return parseToken(accessToken, accessSecret);
    }

    public Claims parseRefreshToken(String refreshToken) {
        return parseToken(refreshToken, refreshSecret);
    }

    public static Key getSigningKey(byte[] secretKey) { return Keys.hmacShaKeyFor(secretKey); }
    }