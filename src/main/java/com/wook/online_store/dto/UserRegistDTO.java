package com.wook.online_store.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRegistDTO {
    private String email;
    private String password;
    private String name;
    private String username;
    private String phoneNumber;
    private String profileImageUrl;
}
