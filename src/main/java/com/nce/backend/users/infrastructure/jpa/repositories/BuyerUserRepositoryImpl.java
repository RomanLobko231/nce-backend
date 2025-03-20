package com.nce.backend.users.infrastructure.jpa.repositories;

import com.nce.backend.users.domain.entities.BuyerUser;
import com.nce.backend.users.domain.repositories.BuyerUserRepository;
import com.nce.backend.users.domain.repositories.UserRepository;
import com.nce.backend.users.infrastructure.jpa.UserJpaEntityMapper;
import com.nce.backend.users.infrastructure.jpa.entities.BuyerJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class BuyerUserRepositoryImpl implements BuyerUserRepository {

    private final UserJpaEntityMapper userMapper;

    private final UserJpaRepository userRepository;

    @Override
    public Optional<BuyerUser> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<BuyerUser> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<BuyerUser> findAll() {
        return List.of();
    }

    @Override
    public BuyerUser save(BuyerUser user) {
        BuyerJpaEntity savedEntity = userRepository.save(userMapper.toBuyerJpaEntity(user));
        return userMapper.toBuyerUserDomainEntity(savedEntity);
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
