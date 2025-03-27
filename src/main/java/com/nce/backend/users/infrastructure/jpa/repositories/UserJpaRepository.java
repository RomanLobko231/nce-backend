package com.nce.backend.users.infrastructure.jpa.repositories;

import com.nce.backend.users.infrastructure.jpa.entities.BuyerJpaEntity;
import com.nce.backend.users.infrastructure.jpa.entities.SellerJpaEntity;
import com.nce.backend.users.infrastructure.jpa.entities.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

    Optional<UserJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(value = """
            SELECT su.*, u.*
            FROM seller su
            JOIN base_user u ON su.seller_id = u.id
            """, nativeQuery = true)
    List<SellerJpaEntity> findAllSellerUsers();

    @Query(value = """
            SELECT bu.*, u.*
            FROM buyer bu
            JOIN base_user u ON bu.buyer_id = u.id
            """, nativeQuery = true)
    List<BuyerJpaEntity> findAllBuyerUsers();

    @Query(value = """
            SELECT su.*, u.*
            FROM seller su
            JOIN base_user u ON su.seller_id = u.id
            WHERE su.seller_id = :id
            """, nativeQuery = true)
    Optional<SellerJpaEntity> findSellerUserById(@Param("id") UUID id);

    @Query(value = """
            SELECT bu.*, u.*
            FROM buyer bu
            JOIN base_user u ON bu.buyer_id = u.id
            WHERE bu.buyer_id = :id
            """, nativeQuery = true)
    Optional<BuyerJpaEntity> findBuyerUserById(@Param("id") UUID id);
}
