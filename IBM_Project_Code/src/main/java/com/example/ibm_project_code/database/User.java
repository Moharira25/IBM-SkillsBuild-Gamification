package com.example.ibm_project_code.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean emailVerified;

    @Column(nullable = false)
    private Timestamp createdDate;

    @Column()
    private Timestamp lastLoginDate;

    @Column(nullable = false)
    private Timestamp lastModifiedDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;

    @OneToMany(cascade = CascadeType.ALL)
    private List<UserCourse> courses = new ArrayList<>();

    @Column
    private int overallPoints = 0;
    @PreUpdate
    protected void onUpdate() {
        lastModifiedDate = Timestamp.from(Instant.now());
    }

    // Constructors, getters and setters will be handled by Lombok

    //A method to check if a course have been started
    public boolean courseStarted(Course course){
        boolean started = false;
        for (UserCourse c: courses){
            if (Objects.equals(course, c.getCourse())){
                return c.getStartDate() != null;
            }

        }
        return started;
    }

    //Getting the UserCourse by the course.
    public UserCourse getUserCourse(Course course){
        for (UserCourse c: courses){
            if (Objects.equals(course, c.getCourse())){
                return c;
            }
        }
        //if the course does not exist
        return null;
    }


}
