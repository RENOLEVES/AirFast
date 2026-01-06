package ca.mcgill.esce321.flightManagement.controller.api;

import ca.mcgill.esce321.flightManagement.config.TokenType;
import ca.mcgill.esce321.flightManagement.controller.request.PersonRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.AuthenticationResponse;
import ca.mcgill.esce321.flightManagement.dto.response.PersonResponse;
import ca.mcgill.esce321.flightManagement.model.Person;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.service.token.CookieServiceImpl;
import ca.mcgill.esce321.flightManagement.service.token.JwtServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class AuthorizationController {

    private final PersonRepository personRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtService;
    private final CookieServiceImpl cookieService;
    private final PasswordEncoder passwordEncoder;


    /**
     * Login endpoint - authenticates user and returns JWT token in cookie
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody PersonRequestDTO request,
            HttpServletResponse response) {

        try {
            // Validate input
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (request.getPassword() == null || request.getPassword().isBlank()) {
                return ResponseEntity.badRequest().body("Password is required");
            }

            // Find user
            Person person = personRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

            // Authenticate
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String accessToken = jwtService.generateToken(person, TokenType.ACCESS_TOKEN);

            // Create response with token
            AuthenticationResponse authResponse = AuthenticationResponse.builder()
                    .token(accessToken)
                    .build();

            // Set token in HTTP-only cookie
            cookieService.addTokenCookie(response, accessToken, TokenType.ACCESS_TOKEN);

            // Build user info response
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", accessToken);
            responseBody.put("user", buildPersonResponse(person));
            responseBody.put("message", "Login successful");

            return ResponseEntity.ok(responseBody);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during login: " + e.getMessage());
        }
    }


    /**
     * Logout endpoint - clears authentication token
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        try {
            // Clear security context
            SecurityContextHolder.clearContext();

            // Clear token cookies
            cookieService.clearTokenCookies(response);

            return ResponseEntity.ok(Map.of("message", "Logout successful"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during logout");
        }
    }

    /**
     * Verify token endpoint - checks if current token is valid
     */
    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(HttpServletRequest request) {
        try {
            // Extract token from cookie
            String token = cookieService.getTokenFromCookie(request, "accessToken");

            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("valid", false, "message", "No token found"));
            }

            // Extract username from token
            String email = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);

            // Find user
            Person person = personRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Validate token
            boolean isValid = jwtService.isTokenValid(token, person, TokenType.ACCESS_TOKEN);

            if (isValid) {
                return ResponseEntity.ok(Map.of(
                        "valid", true,
                        "user", buildPersonResponse(person)
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("valid", false, "message", "Token is invalid or expired"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false, "message", "Token validation failed"));
        }
    }

    /**
     * Get current user endpoint - returns info about currently authenticated user
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        try {
            // Extract token from cookie
            String token = cookieService.getTokenFromCookie(request, "accessToken");

            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Not authenticated");
            }

            // Extract username and get user details
            String email = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
            Person person = personRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            return ResponseEntity.ok(buildPersonResponse(person));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication failed");
        }
    }

    /**
     * Refresh token endpoint - generates a new access token
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {

        try {
            // Extract current token
            String oldToken = cookieService.getTokenFromCookie(request, "accessToken");

            if (oldToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("No token to refresh");
            }

            // Extract user from token
            String email = jwtService.extractUsername(oldToken, TokenType.ACCESS_TOKEN);
            Person person = personRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Generate new token
            String newToken = jwtService.generateToken(person, TokenType.ACCESS_TOKEN);

            // Set new token in cookie
            cookieService.addTokenCookie(response, newToken, TokenType.ACCESS_TOKEN);

            return ResponseEntity.ok(Map.of(
                    "token", newToken,
                    "message", "Token refreshed successfully"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token refresh failed");
        }
    }

    /**
     * Change password endpoint
     */
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody Map<String, String> passwordData,
            HttpServletRequest request) {

        try {
            String currentPassword = passwordData.get("currentPassword");
            String newPassword = passwordData.get("newPassword");

            if (currentPassword == null || newPassword == null) {
                return ResponseEntity.badRequest()
                        .body("Current password and new password are required");
            }

            // Get current user
            String token = cookieService.getTokenFromCookie(request, "accessToken");
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Not authenticated");
            }

            String email = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
            Person person = personRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Verify current password
            if (!passwordEncoder.matches(currentPassword, person.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Current password is incorrect");
            }

            // Update password
            person.setPassword(passwordEncoder.encode(newPassword));
            personRepository.save(person);

            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to change password");
        }
    }

    // Helper method to build PersonResponse
    private PersonResponse buildPersonResponse(Person person) {
        return new PersonResponse(
                person.getId(),
                person.getEmail(),
                person.getFirstName(),
                person.getLastName(),
                person.getTitle()
        );
    }

    // Exception handler
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}