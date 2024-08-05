package com.asr.project.services;

import com.asr.project.dtos.CategoryDto;
import com.asr.project.payloads.PageableResponse;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

    void deleteCategory(String categoryId);

    PageableResponse<CategoryDto> getCategories(int pageNumber, int pageSize, String sortBy, String sortDir);

    CategoryDto getCategory(String categoryId);

    PageableResponse<CategoryDto> searchCategory(String keyword, int pageNumber, int pageSize, String sortBy, String sortDir);

}
