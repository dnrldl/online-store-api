package com.wook.online_store.service;

import com.wook.online_store.dto.CustomUserDetails;
import com.wook.online_store.dto.UserRegistDTO;
import com.wook.online_store.entity.User;
import com.wook.online_store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserRegistDTO userDTO) {

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return;
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setProfileImageUrl(userDTO.getProfileImageUrl());
        user.setRole("ROLE_USER");

        userRepository.save(user);
    }

    public String getCurrentUserInfo() {
        // 현재 인증된 사용자 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // 사용자 객체가 UserDetails 타입이면 꺼내서 사용 가능
            Object principal = authentication.getPrincipal();

            if (principal instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) principal;
                String email = userDetails.getUsername();  // 이메일
                String role = userDetails.getAuthorities().iterator().next().getAuthority();  // 역할(Role)

                // 필요한 경우 추가 사용자 정보 가져오기 가능
                return "Email: " + email + ", Role: " + role;
            } else if (principal instanceof UserDetails) {
                // 만약 다른 UserDetails 타입을 사용하는 경우에도 처리 가능
                UserDetails userDetails = (UserDetails) principal;
                return "Username: " + userDetails.getUsername();
            } else {
                return "Anonymous User"; // 토큰이 없는 유저
            }
        }

        return "No authenticated user";
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
