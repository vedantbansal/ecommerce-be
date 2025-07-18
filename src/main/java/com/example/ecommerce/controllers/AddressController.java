package com.example.ecommerce.controllers;

import com.example.ecommerce.payload.AddressDTO;
import com.example.ecommerce.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.ecommerce.payload.APIResponse;

@RestController
@RequestMapping("/api")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/addresses")
    public ResponseEntity<APIResponse<AddressDTO>> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        AddressDTO dto = addressService.createAddress(addressDTO);
        return ResponseEntity.ok(new APIResponse<>(dto, "Address created successfully", true));
    }

    @GetMapping("/users/address")
    public ResponseEntity<APIResponse<List<AddressDTO>>> getUserAddresses(){
        List<AddressDTO> addresses = addressService.getUserAddresses();
        return ResponseEntity.ok(new APIResponse<>(addresses, "User addresses fetched successfully", true));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<APIResponse<AddressDTO>> getUserAddressesById(@PathVariable Long addressId){
        AddressDTO dto = addressService.getUserAddressesById(addressId);
        return ResponseEntity.ok(new APIResponse<>(dto, "Address fetched successfully", true));
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<APIResponse<AddressDTO>> updateUserAddress(@PathVariable Long addressId,
                                                        @Valid @RequestBody AddressDTO addressDTO){
        AddressDTO dto = addressService.updateUserAddress(addressId, addressDTO);
        return ResponseEntity.ok(new APIResponse<>(dto, "Address updated successfully", true));
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<APIResponse<Void>> deleteUserAddress(@PathVariable Long addressId){
        boolean deleted = addressService.deleteUserAddress(addressId);
        if (deleted) {
            return ResponseEntity.ok(new APIResponse<>(null, "Successfully deleted address with Id: " + addressId, true));
        } else {
            return ResponseEntity.status(403).body(new APIResponse<>(null, "Error occurred while deleting address", false));
        }
    }
}
