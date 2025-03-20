package com.nce.backend.users.domain.repositories;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.users.domain.entities.BuyerUser;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository<T extends User> {
    Optional<T> findById(UUID id);
    Optional<T> findByEmail(String email);
    List<T> findAll();
    T save(T user);
    void deleteById(UUID id);
    boolean existsByEmail(String email);
    boolean existsById(UUID id);
}
