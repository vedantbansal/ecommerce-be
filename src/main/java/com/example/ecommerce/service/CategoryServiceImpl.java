package com.example.ecommerce.service;

import com.example.ecommerce.exceptions.ApiException;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.payload.CategoryResponse;
import com.example.ecommerce.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> categories = categoryPage.getContent();
        if(categories.isEmpty()) throw new ApiException("No categories present");
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = modelMapper.map(categoryPage, CategoryResponse.class);
        categoryResponse.setContent(categoryDTOS);
        return categoryResponse;
    }

    @Override
    public ResponseEntity<CategoryDTO> createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryFromDB != null) throw new ApiException("Category already exists with name " + category.getCategoryName());
        Category savedCategory = categoryRepository.save(category);
        CategoryDTO savedCategoryDto = modelMapper.map(savedCategory, CategoryDTO.class);
        return new ResponseEntity<>(savedCategoryDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CategoryDTO> updateCategory(Long categoryId, String newCategoryName) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if(category == null) throw new ResourceNotFoundException("Category", "categoryId", categoryId);
        Category categoryFromDB = categoryRepository.findByCategoryName(newCategoryName);
        if(categoryFromDB != null) throw new ApiException("Category already exists with name " + newCategoryName);
        category.setCategoryName(newCategoryName);
        Category savedCategory = categoryRepository.save(category);
        CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CategoryDTO> deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if(category == null) throw new ResourceNotFoundException("Category", "categoryId", categoryId);
        categoryRepository.delete(category);
        CategoryDTO deletedCategoryDTO = modelMapper.map(category, CategoryDTO.class);
        return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);
    }
}
