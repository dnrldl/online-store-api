package com.wook.online_store.jwt.provider;

import com.wook.online_store.jwt.util.JWTUtil;
import com.wook.online_store.jwt.token.JwtAuthenticationToken;
import com.wook.online_store.jwt.util.LoginInfoDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JWTUtil jwtUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;

        Claims claims = jwtUtil.parseAccessToken(authenticationToken.getToken());

        String email = claims.getSubject();
        Long userId = claims.get("userId", Long.class);
        String name = claims.get("name", String.class);
        List<GrantedAuthority> authorities = getGrantedAuthorities(claims);

        LoginInfoDto loginInfo = new LoginInfoDto();
        loginInfo.setUserId(userId);
        loginInfo.setEmail(email);
        loginInfo.setName(name);

        return new JwtAuthenticationToken(authorities, loginInfo, null);
    }

    private List<GrantedAuthority> getGrantedAuthorities(Claims claims) {
        List<String> roles = (List<String>) claims.get("roles");
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(()-> role);
        }
        return authorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
