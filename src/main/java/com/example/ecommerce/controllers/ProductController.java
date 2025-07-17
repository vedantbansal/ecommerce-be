package com.example.ecommerce.controllers;

import com.example.ecommerce.constants.AppConstants;
import com.example.ecommerce.constants.ProductConstants;
import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponse;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('merchant')")
    @PostMapping("/merchant/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO,
                                                 @PathVariable Long categoryId){
        return productService.addProduct(productDTO, categoryId);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                                @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                                @PathVariable Long categoryId){
        return productService.getProductsByCategory(pageNumber, pageSize, categoryId);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                          @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                          @RequestParam(defaultValue = ProductConstants.SORT_PRODUCTS_BY) String sortBy,
                                                          @RequestParam(defaultValue = ProductConstants.SORT_DIR) String sortOrder){
        return productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
    }

    @GetMapping("/public/products/keyword")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@RequestParam String keyword,
                                                                @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                                @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize){
        return productService.getProductsByKeyword(keyword, pageNumber, pageSize);
    }


    @PreAuthorize("hasAnyRole('ROLE_MERCHANT', 'ROLE_ADMIN')")
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable Long productId){
        return productService.updateProduct(productDTO, productId);
    }

    @PreAuthorize("hasAnyRole('ROLE_MERCHANT', 'ROLE_ADMIN')")
    @DeleteMapping("/merchant/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        return productService.deleteProduct(productId);
    }

    @PreAuthorize("hasAnyRole('ROLE_MERCHANT', 'ROLE_ADMIN')")
    @PutMapping("/admin/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam("image")MultipartFile image){
        return productService.updateProductImage(productId, image);
    }

}
