package com.nce.backend.cars.infrastructure.jpa;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.valueObjects.Status;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarJpaRepository extends JpaRepository<CarJpaEntity, UUID> {

    //@Query("SELECT CASE COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM CarJpaEntity c WHERE c.registration_number = :registrationNumber")
    @Query(value = "SELECT EXISTS(SELECT 1 FROM car WHERE registration_number = :registrationNumber)", nativeQuery = true)
    boolean existsByRegistrationNumber(@Param("registrationNumber") String registrationNumber);

    @Query(value = "SELECT * FROM car WHERE owner_id = :ownerId", nativeQuery = true)
    Page<CarJpaEntity> findAllByOwnerId(@Param("ownerId") UUID ownerId, Pageable pageable);

    @Query(value = "SELECT * FROM car WHERE status = :status", nativeQuery = true)
    Page<CarJpaEntity> findAllByStatus(@Param("status") String status, Pageable pageable);

    @Query(value = "SELECT * FROM car WHERE owner_id = :ownerId AND status = :status", nativeQuery = true)
    Page<CarJpaEntity> findAllByOwnerAndStatus(@Param("status") String status, @Param("ownerId") UUID ownerId, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE car SET status = :status WHERE id = :carId", nativeQuery = true)
    void updateCarStatusById(@Param("status") String status, @Param("carId") UUID carId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM car WHERE owner_id = :id", nativeQuery = true)
    void deleteByOwnerId(UUID id);
}
