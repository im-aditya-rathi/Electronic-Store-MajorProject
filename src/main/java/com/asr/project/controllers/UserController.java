package com.asr.project.controllers;

import com.asr.project.dtos.UserDto;
import com.asr.project.payloads.ApiResponseMessage;
import com.asr.project.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    //create
    @PostMapping
    ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        UserDto user = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    //update
    @PutMapping("/{userId}")
    ResponseEntity<UserDto> updateUser(@PathVariable String userId, @Valid @RequestBody UserDto userDto) {
        UserDto user = userService.updateUser(userDto, userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    //delete
    @DeleteMapping("/{userId}")
    ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        ApiResponseMessage response = ApiResponseMessage.builder().
                message("User deleted successfully").success(true).
                status(HttpStatus.OK).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //find all users
    @GetMapping
    ResponseEntity<List<UserDto>> getUsers(@RequestParam MultiValueMap<String, String> multiValueMap)
//               @RequestParam( value = "pageNumber", defaultValue = "0") int pageNumber,
//               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
//               @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
//               @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir)
    {

        int pageNumber = Integer.parseInt(multiValueMap.getOrDefault("pageNumber",
                            List.of("1")).get(0));
        int pageSize = Integer.parseInt(multiValueMap.getOrDefault("pageSize",
                            List.of("10")).get(0));
        String sortBy = multiValueMap.containsKey("sortBy") ?
                            multiValueMap.getFirst("sortBy"): "name";
        String sortDir = multiValueMap.containsKey("sortDir") ?
                            multiValueMap.getFirst("sortDir"): "asc";

        List<UserDto> users = userService.getUsers(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    //find user by id
    @GetMapping("/{userId}")
    ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        UserDto user = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    //find user by email
    @GetMapping("email/{email}")
    ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        UserDto user = userService.getUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    //search user
    @GetMapping("search/{keyword}")
    ResponseEntity<List<UserDto>> searchUsers(@PathVariable String keyword) {
        List<UserDto> users = userService.searchUser(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

}
