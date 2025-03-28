package com.nce.backend.users.domain.services;

import com.nce.backend.users.domain.entities.BuyerUser;
import com.nce.backend.users.domain.entities.OneTimeSellerUser;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.domain.repositories.UserRepository;
import com.nce.backend.users.domain.valueObjects.Role;
import com.nce.backend.users.exceptions.UserDoesNotExistException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;

    @Transactional
    public User registerSeller(SellerUser userToSave) {
        userToSave.setRole(Role.SELLER);

        return userRepository.save(userToSave);
    }

    @Transactional
    public User registerBuyer(BuyerUser userToSave) {
        userToSave.setRole(Role.BUYER);
        userToSave.setAccountLocked(true);

        return userRepository.save(userToSave);
    }

    public User saveUser(User userToSave) {
        return userRepository.save(userToSave);
    }

    public User updateUser(User userToSave) {
        if (!userRepository.existsByEmail(userToSave.getEmail())) {
            throw new UserDoesNotExistException("User with this email does not exist");
        }

        return this.saveUser(userToSave);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<SellerUser> findAllSellers() {
        return userRepository.findAllSellerUsers();
    }

    public List<BuyerUser> findAllBuyers() {
        return userRepository.findAllBuyerUsers();
    }

    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<SellerUser> findSellerById(UUID id) {
        return userRepository.findSellerUserById(id);
    }

    public Optional<BuyerUser> findBuyerById(UUID id) {
        return userRepository.findBuyerUserById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void deleteUserById(UUID id) {
        userRepository.deleteById(id);
    }

    public User registerOneTimeSeller(OneTimeSellerUser userToSave) {
        userToSave.setRole(Role.ONE_TIME_SELLER);
        userToSave.setAccountLocked(true);

        return userRepository.save(userToSave);
    }
}
