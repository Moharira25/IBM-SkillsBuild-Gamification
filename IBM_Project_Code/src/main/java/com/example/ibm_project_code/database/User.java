package com.example.ibm_project_code.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Setter for username, if not already present
    @Setter
    @Getter
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column()
    private String bio;

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

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Rating> ratings = new ArrayList<>();

    // Marketplace-specific fields
    // default balance is 0
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    // Associations with other entities
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;

    @OneToMany(cascade = CascadeType.ALL)
    private List<UserCourse> courses = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<BadgeCollection> badgeCollection = new ArrayList<>();

    // This could represent items owned by the user in the marketplace
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserItem> userItems = new ArrayList<>();

    @Column
    private int overallPoints = 0;

    @PreUpdate
    protected void onUpdate() {
        lastModifiedDate = Timestamp.from(Instant.now());
    }

    @OneToMany(mappedBy = "user")
    private List<UserTimeTrial> trials = new ArrayList<>();

    //user trial attempts
    private int attempts = 2;

    //latest trial score for the trials leaderboard
    private int trialScore = 0;

    // Check if a course has been started
    public boolean courseStarted(Course course) {
        for (UserCourse c : courses) {
            if (Objects.equals(course, c.getCourse())) {
                return c.getStartDate() != null;
            }
        }
        return false;
    }

    // Get the UserCourse by the course
    public UserCourse getUserCourse(Course course) {
        for (UserCourse c : courses) {
            if (Objects.equals(course, c.getCourse())) {
                return c;
            }
        }
        // If the course does not exist
        return null;
    }

    // Get the UserTimeTrial by the trial
    public UserTimeTrial getUserTimeTrial(TimeTrial timeTrial) {
        for (UserTimeTrial ut : trials) {
            if (Objects.equals(timeTrial, ut.getTimeTrial())) {
                return ut;
            }
        }
        // If the course does not exist
        return null;
    }

    // Return the highest score for the given old-trial.
    public int getOldTrialScore(TimeTrial timeTrial) {
        int score = 0;
        for (UserTimeTrial ut : trials) {
            if (Objects.equals(timeTrial, ut.getTimeTrial())) {
                if (ut.getScore() > score){
                    score = ut.getScore();
                }
            }
        }
        return score;
    }
    // In User.java
    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Collection<User> friends = new ArrayList<>();
    public void addFriend(User user) {
        this.friends.add(user);
        user.friends.add(this);
    }

    public void removeFriend(User user) {
        this.friends.remove(user);
        user.friends.remove(this);
    }


    public void resetBio() {
        this.bio = "This user has not added to their own Bio.";
    }
}
