package com.sharvan.user_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.sharvan.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.sharvan.user_service.dto.request.UserRequest;
import com.sharvan.user_service.dto.response.UserResponse;
import com.sharvan.user_service.payload.ApiResponse;

import jakarta.validation.Valid;

@Tag(name = "User APIs", description = "Operations related to users")
@RestController
@RequestMapping("/api/users/")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new user", description = "Create a new user with the provided details")
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = service.saveUser(userRequest);

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User created successfully")
                .data(userResponse)
                .timestamp(java.time.LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users with optional sorting")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<UserResponse> userResponse = service.getAllUsers(page, size, sortBy, sortDir);
        ApiResponse<Page<UserResponse>> apiResponse = ApiResponse.<Page<UserResponse>>builder()
                .success(true)
                .message("Users retrieved successfully")
                .data(userResponse)
                .timestamp(java.time.LocalDateTime.now())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Operation
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        UserResponse userResponse = service.getUserById(id);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User found successfully")
                .data(userResponse)
                .timestamp(java.time.LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(@PathVariable Long id,
            @RequestBody UserRequest userRequest) {
        UserResponse updatedUser = service.updateUser(id, userRequest);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User updated successfully")
                .data(updatedUser)
                .timestamp(java.time.LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a user", description = "Delete a user by their ID")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteUser(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Search users", description = "Search users by keyword in first name or email with pagination and sorting")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<UserResponse> userResponse = service.searchUsers(keyword, page, size, sortBy, sortDir);
        ApiResponse<Page<UserResponse>> apiResponse = ApiResponse.<Page<UserResponse>>builder()
                .success(true)
                .message("Users retrieved successfully")
                .data(userResponse)
                .timestamp(java.time.LocalDateTime.now())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}