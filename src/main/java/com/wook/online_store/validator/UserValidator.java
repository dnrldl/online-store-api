package com.wook.online_store.validator;

import com.wook.online_store.dto.UserRegistDTO;
import com.wook.online_store.exception.EmailAlreadyExistsException;
import com.wook.online_store.exception.InvalidPasswordException;
import com.wook.online_store.exception.PhoneNumberAlreadyExistsException;
import com.wook.online_store.exception.UsernameAlreadyExistsException;
import com.wook.online_store.repository.UserRepository;

public class UserValidator {
    private UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUserRegistration(UserRegistDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailAlreadyExistsException("이메일이 이미 존재합니다.");
        }

        if (userRepository.existsByNickname(userDTO.getNickname())) {
            throw new UsernameAlreadyExistsException("닉네임이 이미 존재합니다.");
        }

        if (!isValidPassword(userDTO.getPassword())) {
            throw new InvalidPasswordException("패스워드 형식이 유효하지 않습니다.");
        }

        if (userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException("전화번호가 이미 존재합니다.");
        }
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 4;
    }
}


