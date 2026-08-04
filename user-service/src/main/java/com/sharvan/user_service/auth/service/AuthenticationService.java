package com.sharvan.user_service.auth.service;

import com.sharvan.user_service.auth.dto.LoginRequest;
import com.sharvan.user_service.auth.dto.LoginResponse;
import com.sharvan.user_service.auth.dto.LogoutRequest;
import com.sharvan.user_service.auth.refresh.dto.RefreshTokenRequest;

public interface AuthenticationService {

    LoginResponse authenticate(LoginRequest loginRequest);

    LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    
    void logout(LogoutRequest request);
    
}
