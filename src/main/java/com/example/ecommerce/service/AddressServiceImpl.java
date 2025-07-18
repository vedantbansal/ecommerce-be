package com.example.ecommerce.service;

import com.example.ecommerce.Util.AuthUtils;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.mappers.AddressMapper;
import com.example.ecommerce.model.Address;
import com.example.ecommerce.model.User;
import com.example.ecommerce.payload.AddressDTO;
import com.example.ecommerce.repository.AddressRepository;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AddressServiceImpl implements AddressService{

    AuthUtils authUtils;
    AddressMapper addressMapper;
    AddressRepository addressRepository;
    UserRepository userRepository;

    public AddressServiceImpl(AuthUtils authUtils,
                              AddressMapper addressMapper,
                              AddressRepository addressRepository,
                              UserRepository userRepository) {
        this.authUtils = authUtils;
        this.addressMapper = addressMapper;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        User user = authUtils.getLoggedInUser();
        Address address = addressMapper.toAddress(addressDTO);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toAddressDTO(savedAddress);
    }

    @Override
    public List<AddressDTO> getUserAddresses() {
        User user = authUtils.getLoggedInUser();
        List<Address> addresses = addressRepository.findByUserId(user.getUserId());
        return addresses.stream().map(addressMapper::toAddressDTO).toList();
    }

    @Override
    public AddressDTO updateUserAddress(Long addressId, AddressDTO addressDTO) {
        User user = authUtils.getLoggedInUser();
        Address address = addressRepository.findByUserIdAndAddressId(user.getUserId(), addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        address = addressMapper.toAddress(addressDTO);
        address.setUser(user);
        address.setAddressId(addressId);
        Address savedUser = addressRepository.save(address);
        return addressMapper.toAddressDTO(savedUser);
    }

    @Override
    public boolean deleteUserAddress(Long addressId) {
        User user = authUtils.getLoggedInUser();
        Address address = addressRepository.findByUserIdAndAddressId(user.getUserId(), addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        try {
            user.getAddresses().removeIf(add -> Objects.equals(address.getAddressId(), add.getAddressId()));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public AddressDTO getUserAddressesById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address","addressId",addressId));
        return addressMapper.toAddressDTO(address);
    }
}
