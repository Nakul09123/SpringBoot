package com.example.rating.controller;

import com.example.rating.dto.AuthRequest;
import com.example.rating.entity.Rating;
import com.example.rating.entity.User;
import com.example.rating.service.AuthService;
import com.example.rating.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RatingService ratingService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        return authService.register(user, "ADMIN");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        return authService.login(request);
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
