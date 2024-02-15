package com.example.ibm_project_code.database;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserCourse {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Course course;
    @ManyToOne
    private User user;
    private String startDate;
    private String endDate;
    private boolean isDone = false;

}

