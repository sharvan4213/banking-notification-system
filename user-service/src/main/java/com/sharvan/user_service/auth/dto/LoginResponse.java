package com.sharvan.user_service.auth.dto;

import com.sharvan.user_service.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String refreshToken;
    @Builder.Default 
    private String tokenType = "Bearer";
    
}
