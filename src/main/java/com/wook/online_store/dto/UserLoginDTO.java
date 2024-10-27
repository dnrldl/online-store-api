package com.wook.online_store.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserLoginDTO {
    private String email;
    private String password;
}
