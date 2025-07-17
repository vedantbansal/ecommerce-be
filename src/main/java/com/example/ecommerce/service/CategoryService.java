package com.example.ecommerce.service;

import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.payload.CategoryResponse;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize);
    ResponseEntity<CategoryDTO> createCategory(CategoryDTO categoryDTO);
    ResponseEntity<CategoryDTO> updateCategory(Long categoryId, String newCategoryName);
    ResponseEntity<CategoryDTO> deleteCategory(Long categoryId);
}
