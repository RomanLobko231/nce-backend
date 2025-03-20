package com.nce.backend.users.infrastructure.jpa.repositories;

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
public class UserRepositoryImpl implements UserRepository<User> {

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
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsById(UUID id) {
        return userJpaRepository.existsById(id);
    }
}
