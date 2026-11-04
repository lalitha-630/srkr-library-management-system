package com.srkr.library.repository;

import com.srkr.library.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByRegistrationNumber(String registrationNumber);
}