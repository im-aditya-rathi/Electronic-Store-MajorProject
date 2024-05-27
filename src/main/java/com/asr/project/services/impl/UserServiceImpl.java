package com.asr.project.services.impl;

import com.asr.project.dtos.UserDto;
import com.asr.project.entities.User;
import com.asr.project.exceptions.ResourceNotFoundException;
import com.asr.project.repositories.UserRepository;
import com.asr.project.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {

        //generate random uuid
        String id = UUID.randomUUID().toString();
        userDto.setUserId(id);

        User user = dtoToEntity(userDto);
        userRepository.save(user);

        return entityToDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        User user = userRepository.findById(userId).
                orElseThrow(()->new ResourceNotFoundException("User ID not found"));
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        user.setUserImage(userDto.getUserImage());

        User user1 = userRepository.save(user);

        return entityToDto(user1);
    }

    @Override
    public void deleteUser(String userId) {

        User user = userRepository.findById(userId).
                orElseThrow(()->new ResourceNotFoundException("User ID not found"));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending():
                    Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
        Page<User> page = userRepository.findAll(pageable);
        List<User> users = page.toList();
        return users.stream().map(this::entityToDto).toList();
    }


    @Override
    public UserDto getUserById(String userId) {

        User user = userRepository.findById(userId).
                orElseThrow(()->new ResourceNotFoundException("User ID not found"));

        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {

        User user = userRepository.findByEmail(email).
                orElseThrow(()->new ResourceNotFoundException("User not found with requested email"));

        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {

        List<User> users = userRepository.findByNameContains(keyword);

        return users.stream().map(this::entityToDto).toList();
    }

    private User dtoToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private UserDto entityToDto(User user) {
        return  modelMapper.map(user, UserDto.class);
    }
}
