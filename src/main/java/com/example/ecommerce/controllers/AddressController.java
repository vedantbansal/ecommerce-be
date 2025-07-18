package com.example.ecommerce.controllers;

import com.example.ecommerce.payload.AddressDTO;
import com.example.ecommerce.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid AddressDTO addressDTO){
        return addressService.createAddress(addressDTO);
    }

    @GetMapping("/users/address")
    public ResponseEntity<List<AddressDTO>> getUserAddresses(){
        return addressService.getUserAddresses();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getUserAddressesById(Long addressId){
        return addressService.getUserAddressesById(addressId);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateUserAddress(@PathVariable Long addressId,
                                                        @Valid @RequestBody AddressDTO addressDTO){
        return addressService.updateUserAddress(addressId, addressDTO);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteUserAddress(@PathVariable Long addressId){
        return addressService.deleteUserAddress(addressId);
    }
}
