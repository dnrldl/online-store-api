package com.wook.online_store.dto;

import com.wook.online_store.domain.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserInfoResponseDTO {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String phoneNumber;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private List<Role> roles;
}
