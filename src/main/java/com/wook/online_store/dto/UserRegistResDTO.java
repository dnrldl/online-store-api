package com.wook.online_store.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRegistResDTO {
    private Long userId;
    private String email;
    private String name;
    private String username;
    private LocalDateTime createdAt;
}
