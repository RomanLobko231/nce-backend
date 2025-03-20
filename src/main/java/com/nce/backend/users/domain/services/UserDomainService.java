package com.nce.backend.users.domain.services;

import com.nce.backend.users.domain.entities.BuyerUser;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.domain.repositories.BuyerUserRepository;
import com.nce.backend.users.domain.repositories.SellerUserRepository;
import com.nce.backend.users.domain.repositories.UserRepository;
import com.nce.backend.users.domain.valueObjects.Role;
import com.nce.backend.users.exceptions.UserAlreadyExistsException;
import com.nce.backend.users.exceptions.UserDoesNotExistException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository<User> userRepository;
    private final SellerUserRepository sellerRepository;
    private final BuyerUserRepository buyerRepository;

    @Transactional
    public SellerUser registerSeller(SellerUser userToSave) {
        userToSave.setRole(Role.SELLER);

        return sellerRepository.save(userToSave);
    }

    @Transactional
    public BuyerUser registerBuyer(BuyerUser userToSave) {
        userToSave.setRole(Role.BUYER);
        userToSave.setAccountLocked(true);

        return buyerRepository.save(userToSave);
    }

    public User saveUser(User userToSave) {
        return userRepository.save(userToSave);
    }

    public User updateUser(User userToSave) {
        if (!userRepository.existsByEmail(userToSave.getEmail())) {
            throw new UserDoesNotExistException("User with this email does not exist");
        }

        if (userToSave instanceof SellerUser sellerUser) {
            return sellerRepository.save(sellerUser);
        } else if (userToSave instanceof BuyerUser buyerUser) {
            return buyerRepository.save(buyerUser);
        }

        return this.saveUser(userToSave);
    }

    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<SellerUser> findSellerById(UUID id) {
        return sellerRepository.findById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
