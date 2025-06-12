package com.example.rating.controller;

import com.example.rating.config.JwtUtil;
import com.example.rating.dto.AuthRequest;
import com.example.rating.entity.Rating;
import com.example.rating.entity.User;
import com.example.rating.repositories.UserRepository;
import com.example.rating.service.AuthService;
import com.example.rating.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody User user) {
//        return authService.register(user, "ADMIN");
//    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<String> changeUserRole(
            @PathVariable Long userId,
            @RequestParam String role,
            @RequestHeader("Authorization") String authHeader
    ) {
        if (!role.equals("ADMIN") && !role.equals("USER")) {
            return ResponseEntity.badRequest().body("Invalid role. Use ADMIN or USER.");
        }

        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);

        Optional<User> loggedInUser = userRepository.findByEmail(email);
        if (loggedInUser.isEmpty() || !"ADMIN".equals(loggedInUser.get().getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can change roles.");
        }

        Optional<User> targetUser = userRepository.findById(userId);
        if (targetUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = targetUser.get();
        user.setRole(role);
        userRepository.save(user);

        return ResponseEntity.ok("User role updated to " + role);
    }


    @GetMapping("/ratings")
    public ResponseEntity<List<Rating>> getAllRatings() {
        return ResponseEntity.ok(ratingService.getAllRatings());
    }

    @GetMapping("/ratings/filter")
    public ResponseEntity<List<Rating>> filterRatings(
            @RequestParam(required = false) String food,
            @RequestParam(required = false) Integer foodScore,
            @RequestParam(required = false) String ambience,
            @RequestParam(required = false) Integer ambienceScore,
            @RequestParam(required = false) String service,
            @RequestParam(required = false) Integer serviceScore
    ) {
        return ResponseEntity.ok(
                ratingService.filterRatings(food, foodScore, ambience, ambienceScore, service, serviceScore)
        );
    }

    @DeleteMapping("/ratings/{id}")
    public ResponseEntity<String> deleteRating(@PathVariable Long id) {
        boolean deleted = ratingService.deleteRating(id);
        return deleted ? ResponseEntity.ok("Rating deleted") : ResponseEntity.notFound().build();
    }
}
