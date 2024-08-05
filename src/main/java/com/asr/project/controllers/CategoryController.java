package com.asr.project.controllers;

import com.asr.project.dtos.CategoryDto;
import com.asr.project.payloads.ApiResponseMessage;
import com.asr.project.payloads.ImageResponseMessage;
import com.asr.project.payloads.PageableResponse;
import com.asr.project.services.CategoryService;
import com.asr.project.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Value("${category.profile.image.path}")
    private String imageUploadPath;

    @PostMapping
    ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryDto categoryDto) {

        CategoryDto categoryDto1 = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDto1);
    }

    @PutMapping("/{categoryId}")
    ResponseEntity<CategoryDto> updateCategory(@RequestBody @Valid CategoryDto categoryDto, @PathVariable String categoryId) {

        CategoryDto categoryDto1 = categoryService.updateCategory(categoryDto, categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(categoryDto1);
    }

    @DeleteMapping("/{categoryId}")
    ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {

        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        String fileName = categoryDto.getCoverImage();
        if(fileName != null && !fileName.isBlank()) {
            fileService.deleteFile(imageUploadPath, fileName);
        }
        categoryService.deleteCategory(categoryId);
        ApiResponseMessage response = ApiResponseMessage.builder().
                message("Category deleted successfully").success(true).
                status(HttpStatus.OK).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<PageableResponse<CategoryDto>> getCategories(
               @RequestParam( value = "pageNumber", defaultValue = "1") int pageNumber,
               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
               @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
               @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        return ResponseEntity.status(HttpStatus.OK).
                body(categoryService.getCategories(pageNumber, pageSize, sortBy, sortDir));
    }

    @GetMapping("/{categoryId}")
    ResponseEntity<CategoryDto> getCategories(@PathVariable String categoryId) {

        return ResponseEntity.status(HttpStatus.OK).
                body(categoryService.getCategory(categoryId));
    }

    @GetMapping("search/{keyword}")
    ResponseEntity<PageableResponse<CategoryDto>> searchCategory(@PathVariable String keyword,
                @RequestParam( value = "pageNumber", defaultValue = "1") int pageNumber,
                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
                @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        return ResponseEntity.status(HttpStatus.OK).
                body(categoryService.searchCategory(keyword, pageNumber, pageSize, sortBy, sortDir));
    }


    @PostMapping("/image/{categoryId}")
    ResponseEntity<ImageResponseMessage> updateCategoryImage(@PathVariable String categoryId,
                @RequestParam("image") MultipartFile file) throws IOException {

        String fileName = fileService.uploadFile(file, imageUploadPath);
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        categoryDto.setCoverImage(fileName);
        categoryService.updateCategory(categoryDto, categoryId);

        ImageResponseMessage responseMessage = ImageResponseMessage.builder()
                .message("Image is uploaded successfully").imageName(fileName)
                .status(HttpStatus.CREATED).success(true).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @GetMapping("/image/{categoryId}")
    void serveCategoryImage(@PathVariable String categoryId,
                 HttpServletResponse response) throws IOException {

        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        String fileName = categoryDto.getCoverImage();
        InputStream stream = fileService.getResource(imageUploadPath, fileName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(stream, response.getOutputStream());
    }

}
