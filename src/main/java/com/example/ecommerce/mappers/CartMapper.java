package com.example.ecommerce.mappers;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.payload.CartDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartDTO toCartDTO(Cart cart);
    default String map(Category category) {
        return category != null ? category.getCategoryName() : null;
    }
}
