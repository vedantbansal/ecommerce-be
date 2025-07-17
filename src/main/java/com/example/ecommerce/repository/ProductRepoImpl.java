package com.example.ecommerce.repository;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepoImpl {

//    Page<Product> findByCategory(Category category, Pageable pageable);
//
//    @Query("SELECT p from Product p WHERE LOWER(p.productName) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(concat('%', :keyword ,'%'))")
//    Page<Product> searchProductByKeyword(@Param("keyword") String keyword, Pageable pageable);
//
//    @Override
//    int countProductByNameAndCategory(String productName, String category){
//
//    }
}
