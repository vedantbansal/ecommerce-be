package com.example.ecommerce.controllers;

import com.example.ecommerce.constants.AppConstants;
import com.example.ecommerce.constants.ProductConstants;
import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponse;
import com.example.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.ecommerce.payload.APIResponse;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('merchant')")
    @PostMapping("/merchant/categories/{categoryId}/product")
    public ResponseEntity<APIResponse<ProductDTO>> addProduct(@RequestBody ProductDTO productDTO,
                                                 @PathVariable Long categoryId){
        ProductDTO dto = productService.addProduct(productDTO, categoryId);
        return ResponseEntity.ok(new APIResponse<>(dto, "Product added successfully", true));
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<APIResponse<ProductResponse>> getProductsByCategory(@RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                                @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                                @PathVariable Long categoryId){
        ProductResponse response = productService.getProductsByCategory(pageNumber, pageSize, categoryId);
        return ResponseEntity.ok(new APIResponse<>(response, "Products by category fetched", true));
    }

    @GetMapping("/public/products")
    public ResponseEntity<APIResponse<ProductResponse>> getAllProducts(@RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                          @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                          @RequestParam(defaultValue = ProductConstants.SORT_PRODUCTS_BY) String sortBy,
                                                          @RequestParam(defaultValue = ProductConstants.SORT_DIR) String sortOrder){
        ProductResponse response = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(new APIResponse<>(response, "All products fetched", true));
    }

    @GetMapping("/public/products/keyword")
    public ResponseEntity<APIResponse<ProductResponse>> getProductsByKeyword(@RequestParam String keyword,
                                                                @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                                @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize){
        ProductResponse response = productService.getProductsByKeyword(keyword, pageNumber, pageSize);
        return ResponseEntity.ok(new APIResponse<>(response, "Products by keyword fetched", true));
    }


    @PreAuthorize("hasAnyRole('ROLE_MERCHANT', 'ROLE_ADMIN')")
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<APIResponse<ProductDTO>> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable Long productId){
        ProductDTO dto = productService.updateProduct(productDTO, productId);
        return ResponseEntity.ok(new APIResponse<>(dto, "Product updated successfully", true));
    }

    @PreAuthorize("hasAnyRole('ROLE_MERCHANT', 'ROLE_ADMIN')")
    @DeleteMapping("/merchant/products/{productId}")
    public ResponseEntity<APIResponse<ProductDTO>> deleteProduct(@PathVariable Long productId){
        ProductDTO dto = productService.deleteProduct(productId);
        return ResponseEntity.ok(new APIResponse<>(dto, "Product deleted successfully", true));
    }

    @PreAuthorize("hasAnyRole('ROLE_MERCHANT', 'ROLE_ADMIN')")
    @PutMapping("/admin/{productId}/image")
    public ResponseEntity<APIResponse<ProductDTO>> updateProductImage(@PathVariable Long productId, @RequestParam("image")MultipartFile image){
        ProductDTO dto = productService.updateProductImage(productId, image);
        return ResponseEntity.ok(new APIResponse<>(dto, "Product image updated", true));
    }

}
