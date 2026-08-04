package com.sharvan.user_service.service;

import org.springframework.data.domain.Page;

import com.sharvan.user_service.dto.request.UserRequest;
import com.sharvan.user_service.dto.response.UserResponse;

public interface UserService {
    // Define your service methods here

    UserResponse saveUser(UserRequest userRequest);

    Page<UserResponse> getAllUsers(int page, int size, String sortBy, String sortDir);

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, UserRequest userRequest);

    void deleteUser(Long id);

    Page<UserResponse> searchUsers(
            String keyword,
            int page,
            int size,
            String sortBy,
            String sortDir);
}