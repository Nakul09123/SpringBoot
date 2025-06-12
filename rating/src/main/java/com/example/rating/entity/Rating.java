package com.example.rating.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ambience;

    @Column(nullable = false)
    private int ambienceScore;

    @Column(nullable = false)
    private String food;

    @Column(nullable = false)
    private int foodScore;

    @Column(nullable = false)
    private String service;

    @Column(nullable = false)
    private int serviceScore;


    @ManyToOne
    private User user;

}
