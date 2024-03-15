package com.example.ibm_project_code.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String language;
    private String duration;
    private String title;
    @Column(length = 65555)
    private String description;
    @Column
    private String link;
    private String category;
    @Column
    private int points;
    @Column
    private int courseUsers;

    @OneToMany(cascade = CascadeType.ALL)
    private List<UserCourse> enrollments;
    // Constructors, getters and setters will be handled by Lombok
}

