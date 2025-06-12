package com.example.rating.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.rating.entity.User;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User,Long>{
    Optional<User> findByEmail(String email);

}
