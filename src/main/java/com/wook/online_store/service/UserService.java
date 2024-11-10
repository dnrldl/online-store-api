package com.wook.online_store.service;

import com.wook.online_store.domain.Role;
import com.wook.online_store.domain.User;
import com.wook.online_store.dto.LoginDTO;
import com.wook.online_store.dto.LoginResponseDTO;
import com.wook.online_store.dto.UserInfoResponseDTO;
import com.wook.online_store.dto.UserRegistDTO;
import com.wook.online_store.exception.DuplicateException;
import com.wook.online_store.exception.InvalidDataException;
import com.wook.online_store.jwt.util.JwtProvider;
import com.wook.online_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO authenticateUser(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String accessToken = jwtProvider.generateAccessToken(user.getUsername(), user.getRoles());
        String refreshToken = jwtProvider.generateRefreshToken(user.getUsername());

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    @Transactional
    public void registerUser(UserRegistDTO userRegistDTO) {
        if (userRepository.existsByEmail(userRegistDTO.getEmail())) {
            throw new DuplicateException("이미 사용 중인 이메일입니다.");
        }

        if (userRepository.existsByNickname(userRegistDTO.getNickname())) {
            throw new DuplicateException("닉네임이 이미 사용 중입니다.");
        }

        if (userRepository.existsByPhoneNumber(userRegistDTO.getPhoneNumber())) {
            throw new DuplicateException("전화번호가 이미 사용 중입니다.");
        }

        User user = User.builder()
                .email(userRegistDTO.getEmail())
                .password(passwordEncoder.encode(userRegistDTO.getPassword()))
                .name(userRegistDTO.getName())
                .nickname(userRegistDTO.getNickname())
                .phoneNumber(userRegistDTO.getPhoneNumber())
                .roles(List.of(Role.ROLE_USER))
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new InvalidDataException("해당 이메일의 사용자가 없습니다."));
    }

    @Transactional(readOnly = true)
    public Optional<User> getMemberById(Long userId) {
        return userRepository.findById(userId);
    }


    @Transactional(readOnly = true)
    public UserInfoResponseDTO getUserInfo(String username) {
        User user = findByEmail(username);

        UserInfoResponseDTO userInfo = UserInfoResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .profileImageUrl(user.getProfileImageUrl())
                .createdAt(user.getCreatedAt())
                .roles(user.getRoles())
                .build();
        return userInfo;
    }
}
