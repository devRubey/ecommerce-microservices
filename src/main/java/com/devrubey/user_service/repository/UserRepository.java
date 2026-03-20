package com.devrubey.user_service.repository;

import com.devrubey.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by their email
    Optional<User> findByEmail(String email);

    // Check if email already exists
    boolean existsByEmail(String email);
}