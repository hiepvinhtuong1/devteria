package com.devteria.identity_service.service;

import com.devteria.identity_service.dto.request.UserCreationRequest;
import com.devteria.identity_service.dto.request.UserUpdateRequest;
import com.devteria.identity_service.dto.response.UserResponse;
import com.devteria.identity_service.entity.User;
import com.devteria.identity_service.enums.Role;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.mapper.UserMapper;
import com.devteria.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {


        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTS);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        List<UserResponse> userResponses = new ArrayList<>();
        userRepository.findAll().forEach(user -> userResponses.add(userMapper.toUserResponse(user)));
        return userResponses;
    }

    public UserResponse getMyInfo(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        return userMapper.toUserResponse(user);
    }

    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id).get();
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTS);
        }
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        userMapper.updateUser(user, request);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTS);
        }
        userRepository.deleteById(userId);
    }
}
