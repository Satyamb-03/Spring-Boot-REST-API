package com.example.quiztournament.service;

import com.example.quiztournament.model.User;
import com.example.quiztournament.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register a new user
    public User registerUser (User user) {
        return userRepository.save(user);
    }

    // Authenticate user
    public boolean authenticateUser (String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    // Update user profile
    public User updateUser (Long id, User userDetails) {
        Optional<User> optionalUser  = userRepository.findById(id);
        if (optionalUser .isPresent()) {
            User user = optionalUser .get();
            user.setUsername(userDetails.getUsername());
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setEmail(userDetails.getEmail());
            // Update additional fields as needed
            return userRepository.save(user);
        }
        return null; // Or throw an exception
    }

    // Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Reset password
    public void resetPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            // Logic for sending password reset instructions
        }
    }
}