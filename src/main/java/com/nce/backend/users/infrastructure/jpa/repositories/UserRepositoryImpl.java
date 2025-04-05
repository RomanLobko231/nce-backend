package com.nce.backend.users.infrastructure.jpa.repositories;

import com.nce.backend.users.domain.entities.BuyerUser;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.domain.repositories.UserRepository;
import com.nce.backend.users.infrastructure.jpa.UserJpaEntityMapper;
import com.nce.backend.users.infrastructure.jpa.entities.UserJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Primary
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaEntityMapper entityMapper;

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository
                .findById(id)
                .map(entityMapper::toUserDomainEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository
                .findByEmail(email)
                .map(entityMapper::toUserDomainEntity);
    }

    @Override
    public List<SellerUser> findAllSellerUsers() {
        return userJpaRepository
                .findAllSellerUsers()
                .stream()
                .map(entityMapper::toSellerUserDomainEntity)
                .toList();
    }

    @Override
    public List<BuyerUser> findAllBuyerUsers() {
        return userJpaRepository
                .findAllBuyerUsers()
                .stream()
                .map(entityMapper::toBuyerUserDomainEntity)
                .toList();
    }

    @Override
    public Optional<BuyerUser> findBuyerUserById(UUID id) {
        return userJpaRepository
                .findBuyerUserById(id)
                .map(entityMapper::toBuyerUserDomainEntity);
    }

    @Override
    public Optional<SellerUser> findSellerUserById(UUID id) {
        return userJpaRepository
                .findSellerUserById(id)
                .map(entityMapper::toSellerUserDomainEntity);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository
                .findAll()
                .stream()
                .map(entityMapper::toUserDomainEntity)
                .toList();
    }

    @Override
    public User save(User user) {
        UserJpaEntity savedUser = userJpaRepository.save(entityMapper.toUserJpaEntity(user));
        return entityMapper.toUserDomainEntity(savedUser);
    }

    @Override
    public void deleteById(UUID id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public void setIsAccountLocked(UUID id, boolean isAccountLocked) {
        userJpaRepository.setIsAccountLocked(id, isAccountLocked);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsById(UUID id) {
        return userJpaRepository.existsById(id);
    }

    @Override
    public void deleteOneTimeSellerByCarId(UUID carId) {
        userJpaRepository.deleteOneTimeSellerByCarId(carId);
    }
}
