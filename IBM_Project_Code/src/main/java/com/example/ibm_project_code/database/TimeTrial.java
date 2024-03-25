package com.example.ibm_project_code.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TimeTrial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    @Column(length = 65555)
    private String description;
    @OneToMany
    private List<Question> questionList = new ArrayList<>();
    private LocalDateTime trialStartTime;
    private LocalDateTime trialEndTime;
    private boolean ended = false;
    private int participants = 0;

    @OneToMany(mappedBy = "timeTrial")
    private List<UserTimeTrial> usersTrials = new ArrayList<>();

    @OneToMany
    private List<Course> recommendedCourses = new ArrayList<>();


    public void Started(){
        // Calculate end time of the quiz (7 days from start time)
        trialStartTime = LocalDateTime.now();
        trialEndTime = trialStartTime.plusDays(8);
    }

    public String timeRemaining(){
        // Calculate remaining time for the time-trial
        Duration remainingDuration = Duration.between(LocalDateTime.now(), trialEndTime);
        long remainingDays = remainingDuration.toDays();
        //set the ended attribute dynamically if the difference is less than or equal to 0.
        ended = remainingDays <= 0;

        // returning the string "X days remaining"
        return remainingDays + " days remaining";
    }

}
