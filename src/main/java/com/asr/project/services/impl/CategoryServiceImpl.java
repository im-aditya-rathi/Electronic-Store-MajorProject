package com.asr.project.services.impl;

import com.asr.project.dtos.CategoryDto;
import com.asr.project.entities.Category;
import com.asr.project.exceptions.ResourceNotFoundException;
import com.asr.project.payloads.PageableResponse;
import com.asr.project.repositories.CategoryRepository;
import com.asr.project.services.CategoryService;
import com.asr.project.utils.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        String caterogyId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(caterogyId);
        Category category = dtoToEntity(categoryDto);
        Category categorySaved = categoryRepository.save(category);
        return entityToDto(categorySaved);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {

        Category category = categoryRepository.findById(categoryId).
                orElseThrow(()->new ResourceNotFoundException("Category ID not found"));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category category1 = categoryRepository.save(category);
        return entityToDto(category1);
    }

    @Override
    public void deleteCategory(String categoryId) {

        Category category = categoryRepository.findById(categoryId).
                orElseThrow(()->new ResourceNotFoundException("Category ID not found"));
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getCategories(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = "asc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        return Helper.pageableResponse(page, CategoryDto.class);
    }

    @Override
    public CategoryDto getCategory(String categoryId) {

        Category category = categoryRepository.findById(categoryId).
                orElseThrow(()->new ResourceNotFoundException("Category ID not found"));
        return entityToDto(category);
    }

    @Override
    public PageableResponse<CategoryDto> searchCategory(String keyword, int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = "asc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
        Page<Category> page = categoryRepository.findByTitleContaining(keyword, pageable);
        return Helper.pageableResponse(page, CategoryDto.class);
    }

    private CategoryDto entityToDto(Category category) {

        return modelMapper.map(category, CategoryDto.class);
    }

    private Category dtoToEntity(CategoryDto categoryDto) {

        return modelMapper.map(categoryDto, Category.class);
    }
}
