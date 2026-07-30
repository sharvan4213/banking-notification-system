package com.sharvan.user_service.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.sharvan.user_service.service.UserService;
import com.sharvan.user_service.dto.UserRequest;
import com.sharvan.user_service.dto.UserResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users/")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public UserResponse save(@Valid @RequestBody UserRequest userRequest) {
        return service.saveUser(userRequest);
    }

    @GetMapping
    public List<UserResponse> getAll() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id,
            @RequestBody UserRequest userRequest) {
        return service.updateUser(id, userRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteUser(id);
    }
}