package com.example.spring.interfaces;

import com.example.spring.dtos.UserDto;
import com.example.spring.requests.UserRequest;
import com.example.spring.responses.UserResponse;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers(int page, int limit);
    UserDto createUser(UserDto userDto);
    UserDto getUserById(String id);
    UserDto getUserByEmail(String email);

    UserResponse updateUserById(String id, UserRequest userRequest);

    List<UserDto> deleteUserById(String id);
}
