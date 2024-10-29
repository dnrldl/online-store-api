package com.wook.online_store.service;

import com.wook.online_store.domain.Role;
import com.wook.online_store.domain.User;
import com.wook.online_store.repository.RoleRepository;
import com.wook.online_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public User registerUser(User user) {
        Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
        user.addRole(userRole.get());
        User saveUser = userRepository.save(user);
        return saveUser;
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 없습니다."));
    }

    @Transactional(readOnly = true)
    public Optional<User> getMemberById(Long userId) {
        return userRepository.findById(userId);
    }
}
