package com.wook.online_store.jwt.filter;

import com.wook.online_store.jwt.token.JwtAuthenticationToken;
import com.wook.online_store.jwt.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 요청 헤더에서 토큰을 추출
 * SecurityContextHoler 에서 값을 뽑음
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);

        if (StringUtils.hasText(token)) {
            try {
                Authentication authentication = authenticateToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException e) {
                handleJwtException(response, e);
                return;
            } catch (BadCredentialsException e) {
                log.error("Invalid JWT token: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            } catch (Exception e) {
                log.error("Authentication error: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication error");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        System.out.println("authorization = " + authorization);
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

    private Authentication authenticateToken(String token) {
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(token);
        return authenticationManager.authenticate(authenticationToken);
    }

    private void handleJwtException(HttpServletResponse response, JwtException e) throws IOException {
        String message;
        if (e instanceof ExpiredJwtException) {
            message = "JWT token is expired";
        } else if (e instanceof MalformedJwtException) {
            message = "JWT token is malformed";
        } else if (e instanceof UnsupportedJwtException) {
            message = "JWT token is unsupported";
        } else {
            message = "Invalid JWT token";
        }
        log.error("{}: {}", message, e.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }
}
