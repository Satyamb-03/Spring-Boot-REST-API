package com.example.quiztournament.controller;

import com.example.quiztournament.model.User;
import com.example.quiztournament.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // User Registration
    @PostMapping("/register")
    public ResponseEntity<User> registerUser (@Valid @RequestBody User user) {
        User registeredUser  = userService.registerUser (user);
        return new ResponseEntity<>(registeredUser , HttpStatus.CREATED);
    }

    // User Login
    @PostMapping("/login")
    public ResponseEntity<String> loginUser (@RequestParam String username, @RequestParam String password) {
        boolean isAuthenticated = userService.authenticateUser (username, password);
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    // Update User Profile
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser (@PathVariable Long id, @Valid @RequestBody User user) {
        User updatedUser  = userService.updateUser (id, user);
        return ResponseEntity.ok(updatedUser );
    }

    // Get User by ID
    @GetMapping
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Request Password Reset
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email) {
        userService.resetPassword(email);
        return ResponseEntity.ok("Password reset instructions sent to email");
    }
}