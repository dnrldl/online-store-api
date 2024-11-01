package com.wook.online_store.jwt.util;

import com.wook.online_store.jwt.token.JwtAuthenticationToken;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Collection;
import java.util.Iterator;

/**
 * Spring MVC에서 특정한 메소드의 매개변수에 대한 추가적인 처리를 담당
 * 사용자가 로그인했는지를 확인하고, 로그인한 사용자의 정보를 LoginUserDto 형태로 반환
 */
public class IfLoginArgumentResolver implements HandlerMethodArgumentResolver {

    // Controller에 들어가는 파라미터 값을 검증
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(IfLogin.class) != null // @IfLogin 이 붙었는지
                && parameter.getParameterType() == LoginUserDTO.class; // 파라미터의 타입이 LoginUserDTO 인지
    }

    // 위 메서드가 true 라면 실행
    // 파라미터의 처리를 한 후 넘겨줌
    // 유저 정보 넘겨줌
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = null;

        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
        } catch (Exception e) {
            return null;
        }
        // 로그인 안함
        if (authentication == null) {
            return null;
        }

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        LoginUserDTO loginUserDTO = new LoginUserDTO();

        Object principal = jwtAuthenticationToken.getPrincipal(); // 이메일 값
        if (principal ==null) {
            return null;
        }
        LoginInfoDto loginInfoDto = (LoginInfoDto) principal;

        loginInfoDto.setEmail(loginInfoDto.getEmail());
        loginInfoDto.setUserId(loginInfoDto.getUserId());
        loginInfoDto.setName(loginInfoDto.getName());

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();

        while (iterator.hasNext()) {
            GrantedAuthority grantedAuthority = iterator.next();
            String role = grantedAuthority.getAuthority();
            loginUserDTO.addRole(role);
        }

        return loginUserDTO;
    }
}
