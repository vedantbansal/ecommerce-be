package com.example.ecommerce.controllers;

import com.example.ecommerce.constants.AppConstants;
import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.payload.CategoryResponse;
import com.example.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ecommerce.payload.APIResponse;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/public/categories")
    public ResponseEntity<APIResponse<CategoryResponse>> getAllCategories(@RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                             @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize){
        CategoryResponse response = categoryService.getAllCategories(pageNumber, pageSize);
        return ResponseEntity.ok(new APIResponse<>(response, "All categories fetched", true));
    }

    @PostMapping("/api/admin/category")
    public ResponseEntity<APIResponse<CategoryDTO>> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO dto = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(new APIResponse<>(dto, "Category created successfully", true));
    }

    @PutMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<APIResponse<CategoryDTO>> updateCategory(@PathVariable Long categoryId, @RequestParam String newCategoryName){
        CategoryDTO dto = categoryService.updateCategory(categoryId, newCategoryName);
        return ResponseEntity.ok(new APIResponse<>(dto, "Category updated successfully", true));
    }

    @DeleteMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<APIResponse<CategoryDTO>> deleteCategory(@PathVariable Long categoryId){
        CategoryDTO dto = categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(new APIResponse<>(dto, "Category deleted successfully", true));
    }
}
