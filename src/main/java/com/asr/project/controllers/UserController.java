package com.asr.project.controllers;

import com.asr.project.dtos.UserDto;
import com.asr.project.payloads.ApiResponseMessage;
import com.asr.project.payloads.ImageResponseMessage;
import com.asr.project.payloads.PageableResponse;
import com.asr.project.services.FileService;
import com.asr.project.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

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

        UserDto userDto = userService.getUserById(userId);
        String fileName = userDto.getUserImage();
        if(!fileName.isBlank() && fileName != null) {
            fileService.deleteFile(imageUploadPath, fileName);
        }
        userService.deleteUser(userId);
        ApiResponseMessage response = ApiResponseMessage.builder().
                message("User deleted successfully").success(true).
                status(HttpStatus.OK).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //find all users
    @GetMapping
    ResponseEntity<PageableResponse<UserDto>> getUsers(@RequestParam MultiValueMap<String, String> multiValueMap)
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

        return ResponseEntity.status(HttpStatus.OK).
                body(userService.getUsers(pageNumber, pageSize, sortBy, sortDir));
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
    ResponseEntity<PageableResponse<UserDto>> searchUsers(@PathVariable String keyword,
                @RequestParam( value = "pageNumber", defaultValue = "1") int pageNumber,
                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
                @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        return ResponseEntity.status(HttpStatus.OK).
                body(userService.searchUser(keyword, pageNumber, pageSize, sortBy, sortDir));
    }

    //upload user image
    @PostMapping("/image/{userId}")
    ResponseEntity<ImageResponseMessage> uploadUserImage(
            @PathVariable("userId") String userId,
            @RequestParam("image") MultipartFile file
    ) throws IOException {

        String imageName = fileService.uploadFile(file, imageUploadPath);
        UserDto userDto = userService.getUserById(userId);
        userDto.setUserImage(imageName);
        userService.updateUser(userDto, userId);

        ImageResponseMessage responseMessage = ImageResponseMessage.builder().success(true).
                imageName(imageName).message("Image is uploaded successfully").
                status(HttpStatus.CREATED).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    //Serve user image
    @GetMapping("/image/{userId}")
    void serverUserImage(@PathVariable String userId, HttpServletResponse response)
            throws IOException {

        UserDto userDto = userService.getUserById(userId);
        InputStream stream = fileService.getResource(imageUploadPath, userDto.getUserImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(stream, response.getOutputStream());
    }

}
