package com.nce.backend.users.infrastructure.jpa.repositories;

import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.domain.repositories.SellerUserRepository;
import com.nce.backend.users.infrastructure.jpa.UserJpaEntityMapper;
import com.nce.backend.users.infrastructure.jpa.entities.SellerJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class SellerUserRepositoryImpl implements SellerUserRepository {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaEntityMapper userMapper;


    @Override
    public Optional<SellerUser> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<SellerUser> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<SellerUser> findAll() {
        return List.of();
    }

    @Override
    public SellerUser save(SellerUser user) {
        SellerJpaEntity savedEntity = userJpaRepository.save(userMapper.toSellerJpaEntity(user));
        return userMapper.toSellerUserDomainEntity(savedEntity);
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public boolean existsById(UUID id) {
        return false;
    }
}
