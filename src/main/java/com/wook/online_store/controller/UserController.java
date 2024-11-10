package com.wook.online_store.controller;

import com.wook.online_store.domain.User;
import com.wook.online_store.dto.LoginDTO;
import com.wook.online_store.dto.LoginResponseDTO;
import com.wook.online_store.dto.UserInfoResponseDTO;
import com.wook.online_store.dto.UserRegistDTO;
import com.wook.online_store.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegistDTO userRegistDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.registerUser(userRegistDTO);
        return new ResponseEntity("회원가입 완료", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody @Valid LoginDTO loginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        LoginResponseDTO token = userService.authenticateUser(loginDTO);

        return new ResponseEntity(token, HttpStatus.OK);
    }

    @GetMapping("/mypage")
    public ResponseEntity<?> getMyPage(@AuthenticationPrincipal String username) {
        UserInfoResponseDTO userInfo = userService.getUserInfo(username);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

}
