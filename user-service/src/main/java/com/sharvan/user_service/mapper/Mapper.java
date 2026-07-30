package com.sharvan.user_service.mapper;

import org.springframework.stereotype.Component;

import com.sharvan.user_service.dto.UserRequest;
import com.sharvan.user_service.dto.UserResponse;
import com.sharvan.user_service.model.User;

@Component
public  class Mapper {
    
     public User toEntity(UserRequest request) {

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setPassword(request.getPassword());

        return user;
    }

    public UserResponse toResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
