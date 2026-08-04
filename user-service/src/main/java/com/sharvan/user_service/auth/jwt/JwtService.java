package com.sharvan.user_service.auth.jwt;

public interface JwtService {

    String generateToken(String email);

    String extractUsername(String token);

    boolean isTokenValid(String token);

}