package com.nce.backend.users.domain.repositories;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.users.domain.entities.BuyerCompanyUser;
import com.nce.backend.users.domain.entities.BuyerRepresentativeUser;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    List<SellerUser> findAllSellerUsers();
    List<BuyerCompanyUser> findAllBuyerCompanyUsers();
    Optional<BuyerCompanyUser> findBuyerCompanyUserById(UUID id);
    Optional<BuyerRepresentativeUser> findBuyerRepresentativeById(UUID id);
    Optional<SellerUser> findSellerUserById(UUID id);
    List<User> findAll();
    User save(User user);
    void deleteById(UUID id);
    void setIsAccountLocked(UUID id, boolean isAccountLocked);
    boolean existsByEmail(String email);
    boolean existsById(UUID id);
    void deleteOneTimeSellerByCarId(UUID carId);
    void deleteCarIdFromSeller(UUID carId);
    List<BuyerCompanyUser> findAllBuyerCompanyUsersByLocked(boolean isLocked);
    List<String> findLicencesByBuyerId(UUID buyerId);
}
