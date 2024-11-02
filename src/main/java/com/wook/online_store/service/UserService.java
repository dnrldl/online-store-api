package com.wook.online_store.service;

import com.wook.online_store.domain.Role;
import com.wook.online_store.domain.User;
import com.wook.online_store.exception.DuplicateException;
import com.wook.online_store.exception.InvalidDataException;
import com.wook.online_store.repository.RoleRepository;
import com.wook.online_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateException("이미 사용 중인 이메일입니다.");
        }

        if (userRepository.existsByNickname(user.getNickname())) {
            throw new DuplicateException("닉네임이 이미 사용 중입니다.");
        }

        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new DuplicateException("전화번호가 이미 사용 중입니다.");
        }

        Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
        user.addRole(userRole.get());
        User saveUser = userRepository.save(user);
        return saveUser;
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
    public Set<Role> getUserRoles(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getRoles(); // 사용자의 역할 집합을 반환
        }
        return Collections.emptySet(); // 사용자가 존재하지 않는 경우 빈 집합 반환
    }
}
