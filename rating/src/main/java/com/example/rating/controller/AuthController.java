package com.example.rating.controller;

import com.example.rating.config.JwtUtil;
import com.example.rating.dto.AuthRequest;
import com.example.rating.entity.Rating;
import com.example.rating.entity.User;
import com.example.rating.service.AuthService;
import com.example.rating.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RatingService ratingService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        return authService.register(user, "USER");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("/add-rating")
    public Rating addRating(@RequestBody Rating rating, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);
        return ratingService.addRating(rating, email);
    }
}
