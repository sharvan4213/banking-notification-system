package com.sharvan.user_service.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.sharvan.user_service.auth.dto.LoginRequest;
import com.sharvan.user_service.auth.dto.LoginResponse;
import com.sharvan.user_service.auth.dto.LogoutRequest;
import com.sharvan.user_service.auth.refresh.dto.RefreshTokenRequest;
import com.sharvan.user_service.auth.service.AuthenticationService;
import com.sharvan.user_service.payload.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {

        LoginResponse loginResponse = authenticationService.authenticate(loginRequest);

        ApiResponse<LoginResponse> apiResponse = ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("User authenticated successfully")
                .data(loginResponse)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {

        LoginResponse loginResponse = authenticationService.refreshToken(refreshTokenRequest);

        ApiResponse<LoginResponse> apiResponse = ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("Token refreshed successfully")
                .data(loginResponse)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody LogoutRequest request) {

        authenticationService.logout(request);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("User logged out successfully")
                .build());
    }
}
