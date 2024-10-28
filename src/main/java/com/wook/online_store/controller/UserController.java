package com.wook.online_store.controller;

import com.wook.online_store.dto.UserRegistDTO;
import com.wook.online_store.dto.UserRegistResDTO;
import com.wook.online_store.entity.User;
import com.wook.online_store.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


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



    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
