package com.example.ecommerce.mappers;

import com.example.ecommerce.model.Address;
import com.example.ecommerce.payload.AddressDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDTO toAddressDTO(Address address);
    Address toAddress(AddressDTO addressDTO);
}
