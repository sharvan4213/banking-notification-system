package com.sharvan.user_service.service.impl;

import org.springframework.stereotype.Service;

import com.sharvan.user_service.repository.UserRepository;
import com.sharvan.user_service.model.User;
import com.sharvan.user_service.service.UserService;
import java.util.List;
import com.sharvan.user_service.mapper.Mapper;
import com.sharvan.user_service.dto.UserRequest;
import com.sharvan.user_service.dto.UserResponse;
import com.sharvan.user_service.exception.DuplicateEmailException;
import com.sharvan.user_service.exception.UserNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final Mapper mapper;

    public UserServiceImpl(UserRepository repository, Mapper mapper ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UserResponse saveUser(UserRequest userRequest) {
        User user = mapper.toEntity(userRequest);
        if(user  == null){
            throw new RuntimeException("User request is not properly formed");
        }
        if(repository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException("User with email " + user.getEmail() + " already exists");
        }
        return mapper.toResponse(repository.save(user));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return repository.findAll().stream()
        .map(mapper::toResponse)
        .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Long id) {
        return mapper.toResponse(repository.findById(id).orElseThrow(
            () -> new UserNotFoundException("User not found with id: " + id)
        ));
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest)  {

        User existing = repository.findById(id).orElseThrow(
            () -> new UserNotFoundException("User not found with id: " + id)
        );
        existing.setFirstName(userRequest.getFirstName());
        existing.setLastName(userRequest.getLastName());
        existing.setEmail(userRequest.getEmail());
        existing.setMobile(userRequest.getMobile());

        return mapper.toResponse(repository.save(existing));
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

}