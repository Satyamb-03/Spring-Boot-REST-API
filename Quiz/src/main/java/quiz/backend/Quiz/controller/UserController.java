package quiz.backend.Quiz.controller;

import quiz.backend.Quiz.model.Score;
import quiz.backend.Quiz.model.User;
import quiz.backend.Quiz.services.UserService;
import quiz.backend.Quiz.services.EmailService;
import quiz.backend.Quiz.services.ScoreService;
import quiz.backend.Quiz.utils.JwtUtils; // Utility to generate JWT tokens
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register new user
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            System.out.println("Login attempt for username: " + username);

            User user = userService.findByUsername(username).orElse(null);
            if (user == null) {
                System.out.println("User not found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
            System.out.println("Password match result: " + passwordMatches);

            if (!passwordMatches) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            String token = jwtUtils.generateJwtToken(user.getUsername(),
                    user.getRoles().stream().toList());

            System.out.println("Generated JWT Token: " + token);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("role", user.getRoles().stream().findFirst().orElse("PLAYER"));
            response.put("username", user.getUsername());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log exception details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login");
        }
    }

    // Update user profile
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody User user) {
        userService.updateUserProfile(user);
        return ResponseEntity.ok("Profile updated successfully");
    }

    // Player: View participated tournaments
    @GetMapping("/participated-tournaments")
    public ResponseEntity<?> getParticipatedTournaments(Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        List<Score> scores = scoreService.getScoresByUser(user.getId());
        return ResponseEntity.ok(scores);
    }

    // Request password reset
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        User user = userService.findByEmail(email);
        if (user != null) {
            emailService.initiatePasswordReset(email);
            return ResponseEntity.ok("Password reset instructions have been sent to your email.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with the provided email does not exist.");
        }
    }

    // Endpoint to reset the password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        User user = userService.findByResetToken(token);
        if (user != null) {
            userService.updatePassword(user, newPassword);
            return ResponseEntity.ok("Your password has been reset successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired password reset token.");
        }
    }
}
