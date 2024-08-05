package com.asr.project.services;

import com.asr.project.dtos.UserDto;
import com.asr.project.payloads.PageableResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    //create
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto, String userId);

    //delete
    void deleteUser(String userId);

    //get all users
    PageableResponse<UserDto> getUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single user by id
    UserDto getUserById(String userId);

    //get single user by email
    UserDto getUserByEmail(String email);

    //search user
    PageableResponse<UserDto> searchUser(String keyword, int pageNumber, int pageSize, String sortBy, String sortDir);

}
