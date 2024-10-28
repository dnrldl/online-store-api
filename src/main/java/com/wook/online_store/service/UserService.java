package com.wook.online_store.service;

import com.wook.online_store.entity.Role;
import com.wook.online_store.entity.User;
import com.wook.online_store.repository.RoleRepository;
import com.wook.online_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User registerUser(User user) {
        Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
        user.addRole(userRole.get());
        User saveUser = userRepository.save(user);
        return saveUser;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
