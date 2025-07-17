package com.example.ecommerce.service;

import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ResponseEntity<ProductDTO> addProduct(ProductDTO productDTO, Long categoryId);
    ResponseEntity<ProductResponse> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ResponseEntity<ProductResponse> getProductsByCategory(Integer pageNumber, Integer pageSize, Long categoryId);
    ResponseEntity<ProductResponse> getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize);
    ResponseEntity<ProductDTO> updateProduct(ProductDTO productDTO, Long productId);
    ResponseEntity<ProductDTO> deleteProduct(Long productId);
    ResponseEntity<ProductDTO> updateProductImage(Long productId, MultipartFile image);
}

