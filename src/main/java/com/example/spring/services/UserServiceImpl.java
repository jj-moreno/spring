package com.example.spring.services;

import com.example.spring.dtos.UserDto;
import com.example.spring.entities.User;
import com.example.spring.interfaces.UserService;
import com.example.spring.repositories.UserRepository;
import com.example.spring.requests.UserRequest;
import com.example.spring.responses.UserResponse;
import com.example.spring.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAllUsers(int page, int limit) {
        if (page > 0) {
            page--;
        }
        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<User> userPage = userRepository.findAll(pageableRequest);
        List<User> usersFound = userPage.getContent();
        List<UserDto> userDtos = new ArrayList<>();
        UserDto userDto;
        for (User user : usersFound) {
            userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);

        user.setUserId(Utils.generateUserUUID());
        user.setEncryptedPassword(Utils.generateEncryptedPassword());
        User userCreated = userRepository.save(user);

        UserDto newUserDto = new UserDto();
        BeanUtils.copyProperties(userCreated, newUserDto);
        return newUserDto;
    }


    @Override
    public UserDto getUserById(String id) {
        UserDto newUserDto = new UserDto();
        Long idLong = this.convertIdToLong(id);
        assert idLong != null;

        Optional<User> userOptional = userRepository.findById(idLong);
        if (userOptional.isPresent()) {
            User userFound = userOptional.get();
            BeanUtils.copyProperties(userFound, newUserDto);
        }

        return newUserDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserDto newUserDto = new UserDto();
        User userFound = userRepository.findByEmail(email);
        BeanUtils.copyProperties(userFound, newUserDto);
        return newUserDto;
    }

    @Override
    public UserResponse updateUserById(String id, UserRequest userRequest) {
        Long idLong = this.convertIdToLong(id);
        User userForUpdate = this.userRepository.findById(idLong).get();
        BeanUtils.copyProperties(userRequest, userForUpdate);
        userRepository.save(userForUpdate);
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(userForUpdate, userResponse);
        return userResponse;
    }

    @Override
    public List<UserDto> deleteUserById(String id) {
        Long idLong = this.convertIdToLong(id);
        User userForDelete = this.userRepository.findById(idLong).get();
        userRepository.delete(userForDelete);
        return this.getAllUsers(1, 5);
    }

    private Long convertIdToLong(String id) {
        if (id.matches("\\d+")) {
            return Long.parseLong(id);
        } else {
            return null;
        }
    }
}
