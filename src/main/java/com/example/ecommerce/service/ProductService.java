package com.example.ecommerce.service;

import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);
    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse getProductsByCategory(Integer pageNumber, Integer pageSize, Long categoryId);
    ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize);
    ProductDTO updateProduct(ProductDTO productDTO, Long productId);
    ProductDTO deleteProduct(Long productId);
    ProductDTO updateProductImage(Long productId, MultipartFile image);
}

