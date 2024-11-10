package com.wook.online_store.jwt.filter;

import com.wook.online_store.jwt.util.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * HTTP 요청에서 헤더의 JWT토큰을 추출 후 유효성 검사
 * 인증된 사용자 정보를 SecurityContext에 설정
 *
 * UsernamePasswordAuthenticationToken에서
 * 사용자 이름(username), 비밀번호(password), 권한(Authorities)을 설정할 수 있지만
 * JWT토큰 인증 방식에선 비밀번호를 사용하지 않고 권한이 JWT안에 포함되어있기 때문에 null로 설정
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromHeader(request);

        if (token != null && jwtProvider.validateToken(token, jwtProvider.extractUsername(token))) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(jwtProvider.extractUsername(token), null, null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}

