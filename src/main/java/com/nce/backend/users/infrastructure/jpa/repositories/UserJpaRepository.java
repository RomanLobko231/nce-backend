package com.nce.backend.users.infrastructure.jpa.repositories;

import com.nce.backend.users.infrastructure.jpa.entities.AdminJpaEntity;
import com.nce.backend.users.infrastructure.jpa.entities.buyer.BuyerCompanyJpaEntity;
import com.nce.backend.users.infrastructure.jpa.entities.buyer.BuyerRepresentativeJpaEntity;
import com.nce.backend.users.infrastructure.jpa.entities.seller.SellerJpaEntity;
import com.nce.backend.users.infrastructure.jpa.entities.UserJpaEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

    Optional<UserJpaEntity> findByEmail(String email);

    Optional<UserJpaEntity> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    @Query(value = """
            SELECT su.*, u.*
            FROM seller su
            JOIN base_user u ON su.seller_id = u.id
            """, nativeQuery = true)
    List<SellerJpaEntity> findAllSellerUsers();

    @Query(value = """
            SELECT au.*, u.*
            FROM app_admin au
            JOIN base_user u ON au.admin_id = u.id
            """, nativeQuery = true)
    List<AdminJpaEntity> findAllAdminUsers();

    @Query(value = """
            SELECT bu.*, u.*
            FROM buyer_company bu
            JOIN base_user u ON bu.buyer_company_id = u.id
            """, nativeQuery = true)
    List<BuyerCompanyJpaEntity> findAllBuyerCompanyUsers();

    @Query(value = """
            SELECT bu.*, u.*
            FROM buyer_company bu
            JOIN base_user u ON bu.buyer_company_id = u.id
            WHERE u.is_account_locked = :isLocked
            """, nativeQuery = true)
    List<BuyerCompanyJpaEntity> findAllBuyerCompanyUsersByLocked(@Param("isLocked") boolean isLocked);

    @Query(value = """
            SELECT su.*, u.*
            FROM seller su
            JOIN base_user u ON su.seller_id = u.id
            WHERE su.seller_id = :id
            """, nativeQuery = true)
    Optional<SellerJpaEntity> findSellerUserById(@Param("id") UUID id);

    @Query(value = """
            SELECT bu.*, u.*
            FROM buyer_company bu
            JOIN base_user u ON bu.buyer_company_id = u.id
            WHERE bu.buyer_company_id = :id
            """, nativeQuery = true)
    Optional<BuyerCompanyJpaEntity> findBuyerCompanyUserById(@Param("id") UUID id);

    @Query(value = """
            SELECT bru.*, u.*
            FROM buyer_representative bru
            JOIN base_user u ON bru.buyer_representative_id = u.id
            WHERE bru.buyer_representative_id = :id
            """, nativeQuery = true)
    Optional<BuyerRepresentativeJpaEntity> findBuyerRepresentativeById(@Param("id") UUID id);

    @Modifying
    @Query(value = "UPDATE base_user SET is_account_locked = :isAccountLocked WHERE id = :id", nativeQuery = true)
    void setIsAccountLocked(@Param("id") UUID id, @Param("isAccountLocked") boolean isAccountLocked);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM base_user
            WHERE id IN (SELECT one_time_seller_id FROM one_time_seller WHERE car_id = :carId)
            """, nativeQuery = true)
    void deleteOneTimeSellerByCarId(@Param("carId") UUID carId);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM seller_car_ids WHERE car_id = :carId
            """, nativeQuery = true)
    void deleteCarIdFromSeller(@Param("carId") UUID carId);

    @Query(value = "SELECT url FROM org_licence_urls WHERE buyer_company_id = :buyerId", nativeQuery = true)
    List<String> findLicencesByBuyerId(@Param("buyerId") UUID buyerId);

}
