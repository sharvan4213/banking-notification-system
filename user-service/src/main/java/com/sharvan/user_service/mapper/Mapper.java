package com.sharvan.user_service.mapper;

import org.springframework.stereotype.Component;

import com.sharvan.user_service.dto.request.UserRequest;
import com.sharvan.user_service.dto.response.UserResponse;
import com.sharvan.user_service.enums.Role;
import com.sharvan.user_service.model.User;

@Component
public class Mapper {

    public User toEntity(UserRequest request) {

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setPassword(request.getPassword());
        user.setRole(Role.USER);
        user.setEnabled(true);

        return user;
    }

    public UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .phone(user.getMobile())
                .build();

    }
}
