package com.sharvan.user_service.auth.service.impl;

import com.sharvan.user_service.auth.refresh.reporsitory.RefreshTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sharvan.user_service.auth.dto.LoginRequest;
import com.sharvan.user_service.auth.dto.LoginResponse;
import com.sharvan.user_service.auth.dto.LogoutRequest;
import com.sharvan.user_service.auth.jwt.impl.JwtServiceImpl;
import com.sharvan.user_service.auth.refresh.dto.RefreshTokenRequest;
import com.sharvan.user_service.auth.refresh.entity.RefreshToken;
import com.sharvan.user_service.auth.refresh.service.RefreshTokenService;
import com.sharvan.user_service.auth.service.AuthenticationService;
import com.sharvan.user_service.exception.ResourceNotFoundException;
import com.sharvan.user_service.model.User;
import com.sharvan.user_service.repository.UserRepository;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JwtServiceImpl jwtService, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {

        User user = (User) userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("user email or password is incorrect"));

        // System.out.println("User found: " + user.getEmail() + ", Password: " +
        // user.getPassword());
        boolean matches = passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword());

        if (!matches) {
            throw new ResourceNotFoundException("user email or password is incorrect");
        }

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtService.generateToken(user.getEmail()))
                .tokenType("Bearer")
                .refreshToken(refreshToken.getToken())
                .build();

        return loginResponse;
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken());
        refreshTokenService.verifyExpiration(refreshToken);
        User user = refreshToken.getUser();
        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtService.generateToken(user.getEmail()))
                .refreshToken(refreshToken.getToken())
                .build();
        return loginResponse;
    }

    @Override
    public void logout(LogoutRequest request) {

        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken());

        refreshTokenService.deleteByUser(refreshToken.getUser());
    }

}
