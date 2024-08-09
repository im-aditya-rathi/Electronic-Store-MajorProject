package com.asr.project.controllers;

import com.asr.project.dtos.ProductDto;
import com.asr.project.payloads.ApiResponseMessage;
import com.asr.project.payloads.ImageResponseMessage;
import com.asr.project.payloads.PageableResponse;
import com.asr.project.services.FileService;
import com.asr.project.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
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
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.profile.image.path}")
    private String imageUploadPath;

    @PostMapping
    ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {

        ProductDto productDto1 = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto1);
    }

    @PutMapping("/{productId}")
    ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto,
            @PathVariable String productId) {

        ProductDto productDto1 = productService.updateProduct(productDto, productId);
        return ResponseEntity.status(HttpStatus.OK).body(productDto1);
    }

    @DeleteMapping("/{productId}")
    ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) {

        ProductDto productDto = productService.getProduct(productId);
        String fileName = productDto.getCoverImage();
        if(fileName != null && !fileName.isBlank()) {
            fileService.deleteFile(imageUploadPath, fileName);
        }
        productService.deleteProduct(productId);
        ApiResponseMessage response = ApiResponseMessage.builder().
                message("Product deleted successfully").success(true).
                status(HttpStatus.OK).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    ResponseEntity<ProductDto> getProduct(@PathVariable String productId) {

        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @GetMapping
    ResponseEntity<PageableResponse<ProductDto>> getProducts(
            @RequestParam( value = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(productService.getProducts(pageNumber, pageSize, sortBy, sortDir));
    }

    @GetMapping("/live")
    ResponseEntity<PageableResponse<ProductDto>> getLiveProducts(
            @RequestParam( value = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(productService.getLiveProducts(pageNumber, pageSize, sortBy, sortDir));
    }

    @GetMapping("/search")
    ResponseEntity<PageableResponse<ProductDto>> searchProduct(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam( value = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        return ResponseEntity.status(HttpStatus.OK).
                body(productService.searchProduct(keyword, pageNumber, pageSize, sortBy, sortDir));
    }


    //    ############################### Image Operation ##############################
    @PostMapping("/{productId}/image")
    ResponseEntity<ImageResponseMessage> updateProductImage(@PathVariable String productId,
            @RequestParam("image") MultipartFile file) throws IOException {

        String fileName = fileService.uploadFile(file, imageUploadPath);
        ProductDto productDto = productService.getProduct(productId);
        productDto.setCoverImage(fileName);
        productService.updateProduct(productDto, productId);

        ImageResponseMessage responseMessage = ImageResponseMessage.builder()
                .message("Image is uploaded successfully").imageName(fileName)
                .status(HttpStatus.CREATED).success(true).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @GetMapping("/{productId}/image")
    void serveProductIdImage(@PathVariable String productId, HttpServletResponse response) throws IOException {

        ProductDto productDto = productService.getProduct(productId);
        String fileName = productDto.getCoverImage();
        InputStream stream = fileService.getResource(imageUploadPath, fileName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(stream, response.getOutputStream());
    }
}
