package com.asr.project.services.impl;

import com.asr.project.dtos.ProductDto;
import com.asr.project.entities.Category;
import com.asr.project.entities.Product;
import com.asr.project.exceptions.ResourceNotFoundException;
import com.asr.project.payloads.PageableResponse;
import com.asr.project.repositories.CategoryRepository;
import com.asr.project.repositories.ProductRepository;
import com.asr.project.services.ProductService;
import com.asr.project.utils.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDto createProduct(ProductDto productDto) {

        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        Product product = dtoToEntity(productDto);
        Product productSaved = productRepository.save(product);
        return entityToDto(productSaved);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {

        Product product = productRepository.findById(productId).
                orElseThrow(()-> new ResourceNotFoundException("Product ID not found !!"));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setLive(productDto.getLive());
        product.setQuantity(productDto.getQuantity());
        product.setStock(productDto.getStock());
        product.setCoverImage(productDto.getCoverImage());

        Product product1 = productRepository.save(product);

        return entityToDto(product1);
    }

    @Override
    public void deleteProduct(String productId) {

        Product product = productRepository.findById(productId).
                orElseThrow(()-> new ResourceNotFoundException("Product ID not found !!"));
        productRepository.delete(product);
    }

    @Override
    public PageableResponse<ProductDto> getProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
        Page<Product> products = productRepository.findAll(pageable);

        return Helper.getPageableResponse(products, ProductDto.class);
    }

    @Override
    public ProductDto getProduct(String productId) {

        Product product = productRepository.findById(productId).
                orElseThrow(()-> new ResourceNotFoundException("Product ID not found !!"));
        return entityToDto(product);
    }

    @Override
    public PageableResponse<ProductDto> getLiveProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
        Page<Product> products = productRepository.findByLiveTrue(pageable);

        return Helper.getPageableResponse(products, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchProduct(String keyword, int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
        Page<Product> products = productRepository.findByTitleContaining(keyword, pageable);

        return Helper.getPageableResponse(products, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> findAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {

        Category category = categoryRepository.findById(categoryId).
                orElseThrow(()->new ResourceNotFoundException("Category ID not found !!"));
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
        Page<Product> products = productRepository.findByCategory(category, pageable);
        return Helper.getPageableResponse(products, ProductDto.class);
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {

        Category category = categoryRepository.findById(categoryId).
                orElseThrow(()->new ResourceNotFoundException("Category ID not found !!"));
        Product product = dtoToEntity(productDto);
        product.setProductId(UUID.randomUUID().toString());
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product product1 = productRepository.save(product);
        return entityToDto(product1);
    }

    @Override
    public ProductDto updateCategory(String categoryId, String productId) {

        Product product = productRepository.findById(productId).
                orElseThrow(()-> new ResourceNotFoundException("Product ID not found !!"));
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(()->new ResourceNotFoundException("Category ID not found !!"));
        product.setCategory(category);
        Product product1 = productRepository.save(product);
        return entityToDto(product1);
    }

    private ProductDto entityToDto(Product product) {

        return modelMapper.map(product, ProductDto.class);
    }

    private Product dtoToEntity(ProductDto productDto) {

        return modelMapper.map(productDto, Product.class);
    }
}
