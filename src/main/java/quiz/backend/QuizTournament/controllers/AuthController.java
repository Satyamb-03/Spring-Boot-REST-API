package quiz.backend.QuizTournament.controllers;

import quiz.backend.QuizTournament.models.User;
import quiz.backend.QuizTournament.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        try {
            userService.authenticate(username, password); // Authenticate the user
            return ResponseEntity.ok("Login successful. You can now access the quiz.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials. Please try again.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            userService.registerUser(user); // Register the user
            return ResponseEntity.ok("User registered successfully. Please log in.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Password reset request using username
    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestBody Map<String, String> body) {
        String username = body.get("username");

        try {
            String resetToken = userService.requestPasswordReset(username); // Generate and return the reset token using username
            return ResponseEntity.ok("Password reset requested for user: " + username + ". Use this token to reset your password: " + resetToken);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    // Reset password using username and token
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String token = body.get("token");
        String newPassword = body.get("newPassword");

        boolean isReset = userService.resetPasswordWithUsername(username, token, newPassword);
        if (isReset) {
            return ResponseEntity.ok("Password reset successfully for user: " + username + ". You can now log in with your new password.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token or token expired for user: " + username + ". Please request a new password reset.");
        }
    }

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        return ResponseEntity.ok("Logout successful for user: " + username + ". You have been logged out of the application.");
    }
}
