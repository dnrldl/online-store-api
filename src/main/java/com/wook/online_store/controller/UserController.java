package com.wook.online_store.controller;

import com.wook.online_store.domain.RefreshToken;
import com.wook.online_store.domain.Role;
import com.wook.online_store.dto.*;
import com.wook.online_store.domain.User;
import com.wook.online_store.exception.InvalidDataException;
import com.wook.online_store.jwt.util.JWTUtil;
import com.wook.online_store.service.RefreshTokenService;
import com.wook.online_store.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegistDTO userRegistDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setEmail(userRegistDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistDTO.getPassword()));
        user.setName(userRegistDTO.getName());
        user.setNickname(userRegistDTO.getNickname());
        user.setPhoneNumber(userRegistDTO.getPhoneNumber());

        User saveUser = userService.registerUser(user);

        UserRegistResDTO userRegistResDTO = new UserRegistResDTO();
        userRegistResDTO.setUserId(saveUser.getId());
        userRegistResDTO.setEmail(saveUser.getEmail());
        userRegistResDTO.setName(saveUser.getName());
        userRegistResDTO.setNickname(saveUser.getUsername());
        userRegistResDTO.setCreatedAt(saveUser.getCreatedAt());

        return new ResponseEntity<>(userRegistResDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDTO loginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();

            List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

            String accessToken = jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getName(), roles);
            String refreshToken = jwtUtil.createRefreshToken(user.getId(), user.getEmail(), user.getName(), roles);

            RefreshToken refreshTokenEntity = new RefreshToken();
            refreshTokenEntity.setValue(refreshToken);
            refreshTokenEntity.setUserId(user.getId());
            refreshTokenService.addRefreshToken(refreshTokenEntity);

            UserLoginResDTO loginRes = UserLoginResDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .userId(user.getId())
                    .nickname(user.getNickname())
                    .build();

            return new ResponseEntity<>(loginRes, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new InvalidDataException("잘못된 이메일 또는 비밀번호입니다.");
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        refreshTokenService.deleteRefreshToken(refreshTokenDTO.getRefreshToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
