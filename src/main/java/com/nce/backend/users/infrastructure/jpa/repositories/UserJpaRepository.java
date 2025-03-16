package com.nce.backend.users.infrastructure.jpa.repositories;

import com.nce.backend.users.infrastructure.jpa.entities.BuyerJpaEntity;
import com.nce.backend.users.infrastructure.jpa.entities.SellerJpaEntity;
import com.nce.backend.users.infrastructure.jpa.entities.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

    Optional<UserJpaEntity> findByEmail(String email);
}
