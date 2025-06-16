package com.nce.backend.users.domain.services;

import com.nce.backend.common.event.user.UserDeletedEvent;
import com.nce.backend.users.domain.entities.*;
import com.nce.backend.users.domain.repositories.UserRepository;
import com.nce.backend.users.domain.valueObjects.Role;
import com.nce.backend.users.exceptions.UserDoesNotExistException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public User registerSeller(SellerUser userToSave) {
        userToSave.setRole(Role.SELLER);

        return userRepository.save(userToSave);
    }

    @Transactional
    public User registerBuyerCompany(BuyerCompanyUser userToSave) {
        userToSave.setRole(Role.BUYER_COMPANY);
        userToSave.setAccountLock(true);

        return userRepository.save(userToSave);
    }

    @Transactional
    public User registerRepresentative(BuyerRepresentativeUser representative) {
        representative.setRole(Role.BUYER_REPRESENTATIVE);

        return userRepository.save(representative);
    }

    @Transactional
    public User saveUser(User userToSave) {
        return userRepository.save(userToSave);
    }

    @Transactional
    public User updateUser(User userToSave) {
        return this.saveUser(userToSave);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<SellerUser> findAllSellers() {
        return userRepository.findAllSellerUsers();
    }

    public List<BuyerCompanyUser> findAllBuyerCompanies() {
        return userRepository.findAllBuyerCompanyUsers();
    }

    public List<BuyerCompanyUser> findAllBuyerCompaniesByLocked(Boolean isLocked) {
        return userRepository.findAllBuyerCompanyUsersByLocked(isLocked);
    }

    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    public SellerUser findSellerById(UUID id) {
        return userRepository
                .findSellerUserById(id)
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with id '%s' does not exist".formatted(id))
                );
    }

    public BuyerCompanyUser findBuyerById(UUID id) {
        return userRepository
                .findBuyerCompanyUserById(id)
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with id '%s' does not exist".formatted(id))
                );
    }

    public BuyerRepresentativeUser findRepresentativeById(UUID id) {
        return userRepository
                .findBuyerRepresentativeById(id)
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with id '%s' does not exist".formatted(id))
                );
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findUserByNumber(String number) {
        return userRepository.findByPhoneNumber(number);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean companyHasRepresentativeById(UUID companyId, UUID representativeId) {
        BuyerRepresentativeUser rep = userRepository
                .findBuyerRepresentativeById(representativeId)
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with id '%s' does not exist".formatted(representativeId))
                );

        return rep.getBuyerCompanyId().equals(companyId);
    }

    @Transactional
    public void deleteUserById(UUID id) {
        userRepository.deleteById(id);
        eventPublisher.publishEvent(new UserDeletedEvent(id));
    }

    @Transactional
    public User registerOneTimeSeller(OneTimeSellerUser userToSave) {
        userToSave.setRole(Role.ONE_TIME_SELLER);
        userToSave.setAccountLock(true);

        return userRepository.save(userToSave);
    }

    @Transactional
    public void setIsAccountLocked(UUID id, boolean isAccountLocked) {
        User user = userRepository
                .findById(id)
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with id '%s' does not exist".formatted(id))
                );

        user.setAccountLock(isAccountLocked);
        userRepository.save(user);
    }

    @Transactional
    public void deleteOneTimeSellerByCarId(UUID carId) {
        userRepository.deleteOneTimeSellerByCarId(carId);
    }

    @Transactional
    public void deleteCarIdFromSeller(UUID carId) {
        userRepository.deleteCarIdFromSeller(carId);
    }


    public List<String> findLicencesByBuyerId(UUID id) {
        return userRepository.findLicencesByBuyerId(id);
    }

    @Transactional
    public void addCarIdToSaved(UUID userId, UUID carId) {
        BuyerRepresentativeUser rep = userRepository
                .findBuyerRepresentativeById(userId)
                .orElseThrow(
                        () -> new UserDoesNotExistException("Representative with id '%s' does not exist".formatted(userId))
                );

        rep.addNewSavedCar(carId);

        userRepository.save(rep);
    }
}
