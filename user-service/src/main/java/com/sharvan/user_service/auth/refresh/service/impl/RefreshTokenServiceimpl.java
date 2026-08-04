package com.sharvan.user_service.auth.refresh.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.sharvan.user_service.auth.refresh.entity.RefreshToken;
import com.sharvan.user_service.auth.refresh.reporsitory.RefreshTokenRepository;
import com.sharvan.user_service.auth.refresh.service.RefreshTokenService;
import com.sharvan.user_service.exception.RefreshTokenException;
import com.sharvan.user_service.model.User;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenServiceimpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private static final long REFRESH_TOKEN_DURATION_DAYS = 7;

    public RefreshTokenServiceimpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;

    }

    @Override
    public RefreshToken createRefreshToken(User user) {

        refreshTokenRepository.findByUser(user)
                .ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(
                java.time.LocalDateTime.now().plus(java.time.Duration.ofDays(REFRESH_TOKEN_DURATION_DAYS)));
        refreshToken.setToken(java.util.UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            refreshTokenRepository.delete(refreshToken);

            throw new RefreshTokenException(
                    "Refresh token has expired. Please login again.");
        }

        return refreshToken;
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException("Refresh token not found"));
    }

    @Override
    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

}
