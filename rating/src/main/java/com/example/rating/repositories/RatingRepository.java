package com.example.rating.repositories;

import com.example.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RatingRepository extends JpaRepository<Rating, Long> , JpaSpecificationExecutor<Rating> {
}
