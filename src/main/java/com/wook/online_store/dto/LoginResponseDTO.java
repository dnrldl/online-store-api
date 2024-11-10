package com.wook.online_store.dto;

import lombok.*;

//@Builder
@Data
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;

    public LoginResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
