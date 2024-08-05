package com.asr.project.services;

import com.asr.project.dtos.ProductDto;
import com.asr.project.payloads.PageableResponse;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto, String productId);

    void deleteProduct(String productId);

    PageableResponse<ProductDto> getProducts(int pageNumber, int pageSize, String sortBy, String sortDir);

    ProductDto getProduct(String productId);

    PageableResponse<ProductDto> getLiveProducts(int pageNumber, int pageSize, String sortBy, String sortDir);

    PageableResponse<ProductDto> searchProduct(String keyword, int pageNumber, int pageSize, String sortBy, String sortDir);

}
