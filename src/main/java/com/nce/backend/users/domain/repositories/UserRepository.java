package com.nce.backend.users.domain.repositories;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.users.domain.entities.BuyerUser;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    List<SellerUser> findAllSellerUsers();
    List<BuyerUser> findAllBuyerUsers();
    Optional<BuyerUser> findBuyerUserById(UUID id);
    Optional<SellerUser> findSellerUserById(UUID id);
    List<User> findAll();
    User save(User user);
    void deleteById(UUID id);
    boolean existsByEmail(String email);
    boolean existsById(UUID id);
}
