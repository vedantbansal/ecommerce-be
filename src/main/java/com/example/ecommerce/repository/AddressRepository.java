package com.example.ecommerce.repository;

import com.example.ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.user.id=?1")
    List<Address> findByUserId(Long userId);

    @Query("SELECT a FROM Address a WHERE a.user.id=?1 AND a.addressId=?2")
    Optional<Address> findByUserIdAndAddressId(Long userId, Long addressId);
}
