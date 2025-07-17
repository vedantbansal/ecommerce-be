package com.example.ecommerce.controllers;

import com.example.ecommerce.constants.AppConstants;
import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.payload.CategoryResponse;
import com.example.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                             @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize){
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber, pageSize), HttpStatus.OK);
    }

    @PostMapping("/api/admin/category")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        return categoryService.createCategory(categoryDTO);
    }

    @PutMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId, @RequestParam String newCategoryName){
        return categoryService.updateCategory(categoryId, newCategoryName);
    }

    @DeleteMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
        return categoryService.deleteCategory(categoryId);
    }
}
