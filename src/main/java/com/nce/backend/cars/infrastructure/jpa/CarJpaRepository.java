package com.nce.backend.cars.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarJpaRepository extends JpaRepository<CarJpaEntity, UUID> {

    //@Query("SELECT CASE COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM CarJpaEntity c WHERE c.registration_number = :registrationNumber")
    @Query(value = "SELECT EXISTS(SELECT 1 FROM car WHERE registration_number = :registrationNumber)", nativeQuery = true)
    boolean existsByRegistrationNumber(@Param("registrationNumber") String registrationNumber);
}
