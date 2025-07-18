package com.example.ecommerce.service;

import com.example.ecommerce.payload.AddressDTO;
import jakarta.validation.Valid;
import java.util.List;

public interface AddressService {
    AddressDTO createAddress(@Valid AddressDTO addressDTO);
    List<AddressDTO> getUserAddresses();
    AddressDTO updateUserAddress(Long addressId, @Valid AddressDTO addressDTO);
    boolean deleteUserAddress(Long addressId);
    AddressDTO getUserAddressesById(Long addressId);
}
