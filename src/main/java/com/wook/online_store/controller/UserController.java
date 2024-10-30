package com.wook.online_store.controller;

import com.wook.online_store.domain.RefreshToken;
import com.wook.online_store.domain.Role;
import com.wook.online_store.dto.*;
import com.wook.online_store.domain.User;
import com.wook.online_store.jwt.util.JWTUtil;
import com.wook.online_store.service.RefreshTokenService;
import com.wook.online_store.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegistDTO userRegistDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setEmail(userRegistDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistDTO.getPassword()));
        user.setName(userRegistDTO.getName());
        user.setUsername(userRegistDTO.getUsername());
        user.setPhoneNumber(userRegistDTO.getPhoneNumber());

        User saveUser = userService.registerUser(user);

        UserRegistResDTO userRegistResDTO = new UserRegistResDTO();
        userRegistResDTO.setUserId(saveUser.getId());
        userRegistResDTO.setEmail(saveUser.getEmail());
        userRegistResDTO.setName(saveUser.getName());
        userRegistResDTO.setUsername(saveUser.getUsername());
        userRegistResDTO.setCreatedAt(saveUser.getCreatedAt());

        return new ResponseEntity<>(userRegistResDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDTO loginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByEmail(loginDTO.getEmail());

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

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
                .username(user.getUsername())
                .build();

        return new ResponseEntity<>(loginRes, HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        refreshTokenService.deleteRefreshToken(refreshTokenDTO.getRefreshToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
