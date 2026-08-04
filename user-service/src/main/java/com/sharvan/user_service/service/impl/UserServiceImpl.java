package com.sharvan.user_service.service.impl;

import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sharvan.user_service.repository.UserRepository;
import com.sharvan.user_service.model.User;
import com.sharvan.user_service.service.UserService;
import com.sharvan.user_service.mapper.Mapper;
import com.sharvan.user_service.dto.request.UserRequest;
import com.sharvan.user_service.dto.response.UserResponse;
import com.sharvan.user_service.exception.DuplicateEmailException;
import com.sharvan.user_service.exception.UserNotFoundException;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final Mapper mapper;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, Mapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse saveUser(UserRequest userRequest) {
        User user = mapper.toEntity(userRequest);
        if (user == null) {
            logger.error("User request is not properly formed: {}", userRequest);
            throw new RuntimeException("User request is not properly formed");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (repository.existsByEmail(user.getEmail())) {
            logger.error("Duplicate email found: {}", user.getEmail());
            throw new DuplicateEmailException("User with email " + user.getEmail() + " already exists");
        }
        logger.info("Saving user: {}", user.getFirstName());
        return mapper.toResponse(repository.save(user));
    }

    @Override
    public Page<UserResponse> getAllUsers(int page,
            int size,
            String sortBy,
            String sortDir) {

        logger.info("Fetching users with page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        String sortField = "id";
        if ("name".equalsIgnoreCase(sortBy)) {
            sortField = "firstName";
        } else {
            sortField = sortBy;
        }
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> userPage = repository.findAll(pageable);
        return userPage.map(mapper::toResponse);
    }

    @Override
    public UserResponse getUserById(Long id) {
        logger.info("Fetching user with id: {}", id);
        return mapper.toResponse(repository.findById(id).orElseThrow(
                () -> {
                    logger.error("User not found with id: {}", id);
                    return new UserNotFoundException("User not found with id: " + id);
                }));
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        logger.info("Updating user with id: {}", id);

        User existing = repository.findById(id).orElseThrow(
                () -> {
                    logger.error("User not found with id: {}", id);
                    return new UserNotFoundException("User not found with id: " + id);
                });
        if (repository.existsByEmail(userRequest.getEmail()) &&
                !existing.getEmail().equals(userRequest.getEmail())) {
            logger.warn("Duplicate email found: {}", userRequest.getEmail());
            throw new DuplicateEmailException("User with email " + userRequest.getEmail() + " already exists");
        }

        existing.setFirstName(userRequest.getFirstName());
        existing.setLastName(userRequest.getLastName());
        existing.setEmail(userRequest.getEmail());
        existing.setMobile(userRequest.getMobile());

        return mapper.toResponse(repository.save(existing));
    }

    @Override
    public void deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);
        repository.deleteById(id);
        logger.info("User with id: {} deleted successfully", id);
    }

    @Override
    public Page<UserResponse> searchUsers(String keyword, int page, int size, String sortBy, String sortDir) {
        logger.info("Searching users with keyword: {}", keyword);
        String sortField = "id";
        if ("name".equalsIgnoreCase(sortBy)) {
            sortField = "firstName";
        } else {
            sortField = sortBy;
        }
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.searchUsers(keyword, pageable)
                .map(mapper::toResponse);
    }

}