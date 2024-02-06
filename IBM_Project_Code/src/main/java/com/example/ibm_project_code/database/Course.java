package com.example.ibm_project_code.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue
    private int id;
    private String language;
    private String duration;
    private String title;
    @Column(length = 65555)
    private String description;
    @Column
    private String link;
    private String category;

    @ManyToMany(mappedBy = "courses")
    private List<User> users = new ArrayList<>();

    // Constructors, getters and setters will be handled by Lombok
}
