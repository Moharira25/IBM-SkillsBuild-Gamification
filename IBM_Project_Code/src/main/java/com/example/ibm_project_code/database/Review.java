package com.example.ibm_project_code.database;

import jakarta.persistence.*;

@Entity
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewid;
    private String comment;
    @ManyToOne
    private User user;

}
