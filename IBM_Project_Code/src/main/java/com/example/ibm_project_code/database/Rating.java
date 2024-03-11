package com.example.ibm_project_code.database;

import jakarta.persistence.*;

@Entity
public class Rating{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ratingid;
    private int stars;
    @ManyToOne
    private User user;

}
