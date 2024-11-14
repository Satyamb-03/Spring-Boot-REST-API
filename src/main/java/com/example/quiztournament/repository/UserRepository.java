package com.example.quiztournament.repository;

import com.example.quiztournament.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query method to find a user by username
    User findByUsername(String username);

    // Custom query method to find a user by email
    User findByEmail(String email);
}