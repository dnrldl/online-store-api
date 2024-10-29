package com.wook.online_store.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RefreshTokenDTO {
    @NotEmpty
    String refreshToken;
}
