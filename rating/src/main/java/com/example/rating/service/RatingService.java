package com.example.rating.service;

import com.example.rating.entity.Rating;
import com.example.rating.entity.User;
import com.example.rating.repositories.RatingRepository;
import com.example.rating.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    public Rating addRating(Rating rating, String userEmail) {
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found with email: " + userEmail);
        }

        if (rating.getAmbience() == null || rating.getAmbience().isEmpty()
                || rating.getFood() == null || rating.getFood().isEmpty()
                || rating.getService() == null || rating.getService().isEmpty()
                || rating.getAmbienceScore() <= 0
                || rating.getFoodScore() <= 0
                || rating.getServiceScore() <= 0) {
            throw new IllegalArgumentException("All fields must be filled and scores must be greater than 0");
        }

        rating.setUser(userOpt.get());
        return ratingRepository.save(rating);
    }


    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public List<Rating> filterRatings(
            String food,
            Integer foodScore,
            String ambience,
            Integer ambienceScore,
            String service,
            Integer serviceScore
    ) {
        return ratingRepository.findAll().stream()
                .filter(rating -> {
                    boolean matches = true;

                    if (food != null && !food.isEmpty()) {
                        matches &= rating.getFood().toLowerCase().contains(food.toLowerCase());
                    }

                    if (foodScore != null) {
                        matches &= rating.getFoodScore() == foodScore;
                    }

                    if (ambience != null && !ambience.isEmpty()) {
                        matches &= rating.getAmbience().toLowerCase().contains(ambience.toLowerCase());
                    }

                    if (ambienceScore != null) {
                        matches &= rating.getAmbienceScore() == ambienceScore;
                    }

                    if (service != null && !service.isEmpty()) {
                        matches &= rating.getService().toLowerCase().contains(service.toLowerCase());
                    }

                    if (serviceScore != null) {
                        matches &= rating.getServiceScore() == serviceScore;
                    }

                    return matches;
                })
                .collect(Collectors.toList());
    }

    public boolean deleteRating(Long id) {
        if (ratingRepository.existsById(id)) {
            ratingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

