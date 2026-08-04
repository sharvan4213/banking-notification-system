package com.sharvan.user_service.auth.refresh.service;

import com.sharvan.user_service.auth.refresh.entity.RefreshToken;
import com.sharvan.user_service.model.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyExpiration(RefreshToken refreshToken);

    RefreshToken findByToken(String token);

    void deleteByUser(User user);
}
