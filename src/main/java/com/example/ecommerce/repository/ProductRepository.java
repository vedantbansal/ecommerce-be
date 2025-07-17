package com.example.ecommerce.repository;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategory(Category category, Pageable pageable);

    @Query("SELECT p from Product p WHERE LOWER(p.productName) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(concat('%', :keyword ,'%'))")
    Page<Product> searchProductByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.categoryName = :categoryName AND p.productName = :productName")
    int countProductByNameAndCategory(@Param("productName") String productName, @Param("categoryName")  String category);
}
