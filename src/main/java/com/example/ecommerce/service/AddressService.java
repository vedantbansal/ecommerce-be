package com.example.ecommerce.service;

import com.example.ecommerce.payload.AddressDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AddressService {
    ResponseEntity<AddressDTO> createAddress(@Valid AddressDTO addressDTO);

    ResponseEntity<List<AddressDTO>> getUserAddresses();

    ResponseEntity<AddressDTO> updateUserAddress(Long addressId, @Valid AddressDTO addressDTO);

    ResponseEntity<String> deleteUserAddress(Long addressId);

    ResponseEntity<AddressDTO> getUserAddressesById(Long addressId);
}
