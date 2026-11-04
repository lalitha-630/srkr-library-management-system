package com.srkr.library.service;

import com.srkr.library.model.UserAccount;
import com.srkr.library.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public Optional<UserAccount> findByRegistrationNumber(String registrationNumber) {
        return userAccountRepository.findByRegistrationNumber(registrationNumber);
    }

    public UserAccount saveAccount(UserAccount account) {
        return userAccountRepository.save(account);
    }
}