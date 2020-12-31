package com.example.spring.controllers;

import com.example.spring.dtos.UserDto;
import com.example.spring.interfaces.UserService;
import com.example.spring.requests.UserRequest;
import com.example.spring.responses.UserResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users") // localhost:8080/users/
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "limit", defaultValue = "1") int limit) {

        List<UserDto> userDtos = userService.getAllUsers(page, limit);
        List<UserResponse> userResponses = new ArrayList<>();
        UserResponse userResponse;
        for (UserDto userDto : userDtos) {
            userResponse = new UserResponse();
            BeanUtils.copyProperties(userDto, userResponse);
            userResponses.add(userResponse);
        }
        return userResponses;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserResponse createUser(@RequestBody UserRequest userRequest) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRequest, userDto);

        UserDto updatedUserDto = userService.createUser(userDto);

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(updatedUserDto, userResponse);
        return userResponse;
    }

    @GetMapping(params = "id") // localhost:8080/users/?id=1
    public UserResponse getUserById(@RequestParam(name = "id") String id) {
        UserDto userDtoFound = userService.getUserById(id);
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(userDtoFound, userResponse);
        return userResponse;
    }

    @GetMapping(params = "email") // localhost:8080/users/?email=example@tekcamp.org
    public UserResponse getUserByEmail(@RequestParam(name = "email") String email) {
        UserDto userDtoFound = userService.getUserByEmail(email);
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(userDtoFound, userResponse);
        return userResponse;
    }

    @PutMapping(params = "id") // localhost:8080/users/?id=1
    public UserResponse updateUserById(@RequestParam(name = "id") String id, @RequestBody UserRequest userRequest) {
        return userService.updateUserById(id, userRequest);
    }

    @DeleteMapping(params = "id") // localhost:8080/users/?id=1
    public List<UserResponse> deleteUserById(@RequestParam(name = "id") String id) {
        userService.deleteUserById(id);
        return this.getAllUsers(1, 5);
    }
}
