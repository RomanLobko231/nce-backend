package com.nce.backend.users.domain.services;

import com.nce.backend.users.domain.entities.BuyerUser;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.domain.repositories.BuyerUserRepository;
import com.nce.backend.users.domain.repositories.SellerUserRepository;
import com.nce.backend.users.domain.repositories.UserRepository;
import com.nce.backend.users.domain.valueObjects.Role;
import com.nce.backend.users.exceptions.UserAlreadyExistsException;
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
    public SellerUser saveSeller(SellerUser userToSave) {
        checkExistsByEmail(userToSave.getEmail());
        userToSave.setRole(Role.SELLER);

        return sellerRepository.save(userToSave);
    }

    @Transactional
    public BuyerUser saveBuyer(BuyerUser userToSave) {
        checkExistsByEmail(userToSave.getEmail());
        userToSave.setRole(Role.BUYER);

        return buyerRepository.save(userToSave);
    }

    public User saveUser(User userToSave) {
        checkExistsByEmail(userToSave.getEmail());

        return userRepository.save(userToSave);
    }

    public Optional<SellerUser> findSellerById(UUID id) {
        return sellerRepository.findById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private void checkExistsByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
    }
}
