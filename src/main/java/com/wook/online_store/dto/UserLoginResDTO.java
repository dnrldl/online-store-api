package com.wook.online_store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResDTO {
    private String accessToken;
    private String refreshToken;

    private Long userId;
    private String nickname;
}
