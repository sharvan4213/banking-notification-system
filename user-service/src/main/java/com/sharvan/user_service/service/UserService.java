package com.sharvan.user_service.service;

import java.util.List;
import com.sharvan.user_service.dto.UserRequest;
import com.sharvan.user_service.dto.UserResponse;


public interface UserService {
    // Define your service methods here

    UserResponse saveUser(UserRequest userRequest);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, UserRequest userRequest);

    void deleteUser(Long id);
}