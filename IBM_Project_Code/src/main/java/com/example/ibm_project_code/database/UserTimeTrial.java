package com.example.ibm_project_code.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserTimeTrial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private TimeTrial timeTrial;

    private int score = 0;

    @OneToMany
    List<Question> questions;

    @OneToMany(cascade = CascadeType.ALL)
    List<Answer> answers = new ArrayList<>();




    public void finalScore(){
        int i = 0;
        List<Question> trialQuestions = timeTrial.getQuestionList();
        for (Answer answer: answers){
            // if the question is null then the user did not answer the question,
            // and it is automatically 0. in this case we skip the iteration.
            if (!Objects.equals(answer, null)){
                //checking if the users answer is correct
                if (Objects.equals(answer.getAnswer(), trialQuestions.get(i).getAnswer())){
                    if (i == 0){
                        //the first question has a score of 2 to keep all scores integers
                        score += 2;
                    }
                    else {
                        score += 7;
                    }
                }
            }
            i += 1;
        }
    }

}
