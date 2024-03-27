package com.example.ibm_project_code.controllers;

import com.example.ibm_project_code.database.Answer;
import com.example.ibm_project_code.database.TimeTrial;
import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.database.UserTimeTrial;
import com.example.ibm_project_code.repositories.QuestionsRepository;
import com.example.ibm_project_code.repositories.TimeTrialRepository;
import com.example.ibm_project_code.repositories.UserRepository;
import com.example.ibm_project_code.repositories.UserTrialRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class TimeTrialsController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TimeTrialRepository timeTrialRepository;

    @Autowired
    private UserTrialRepository userTrialRepository;

    @RequestMapping("/trials")
    public String trials(Model model) {
        User user = userAuth();
        List<TimeTrial> trials = (List<TimeTrial>) timeTrialRepository.findAll();
        trials.sort(Comparator.comparing(TimeTrial::getTrialStartTime));

        //Getting the active trial by checking the attribute ended.
        TimeTrial timeTrial = timeTrialRepository.findByEndedFalse();

        model.addAttribute("user", user);
        model.addAttribute("userId", user.getId());
        model.addAttribute("trial", timeTrial);
        //check if the timeTrial is not null i.e. ended before finding the user time trial
        UserTimeTrial ut = timeTrial != null? user.getUserTimeTrial(timeTrial): null;
        model.addAttribute("userTrial", ut);
        model.addAttribute("oldTrials", user.getTrials());
        //adding all trials to model
        Collections.reverse(trials);
        model.addAttribute("trials", trials);
        return "timeTrials";
    }

    @RequestMapping("/trials/quiz/{trialId}")
    public String startTrial(Model model, @PathVariable Long trialId){
        User user = userAuth();
        //getting the trial by its id
        TimeTrial timeTrial = timeTrialRepository.findById(trialId).get();

        //Checking if the user has attempts remaining.
        //If not they will be redirected back to the same trial page.
        int attempts = user.getAttempts();
        if (attempts <= 0){
            return "redirect:/trials";
        }

        UserTimeTrial userTimeTrial = new UserTimeTrial();
        //adding 15 empty answers to the new user trial
        for (int i = 0; i < userTimeTrial.getAnswers().size(); i ++){
            Answer answer = new Answer();
            userTimeTrial.getAnswers().add(answer);
        }

        model.addAttribute("userTrial", userTimeTrial);
        model.addAttribute("questions", timeTrial.getQuestionList());
        model.addAttribute("user", user);
        model.addAttribute("userId", user.getId());
        model.addAttribute("review", false);
        return "trialQuiz";
    }


    @PostMapping("/submit")
    public String submitTrial(@ModelAttribute UserTimeTrial userTimeTrial, Model model, HttpServletRequest request){
        User user = userAuth();
        TimeTrial timeTrial = timeTrialRepository.findByEndedFalse();

        //Increasing the participants (once) for every user.
        if (user.getUserTimeTrial(timeTrial) == null){
            timeTrial.setParticipants(timeTrial.getParticipants() + 1);
        }

        for (int i = 0; i < userTimeTrial.getAnswers().size(); i ++){
            String question = "question_%s".formatted(i);
            userTimeTrial.getAnswers().get(i).setQuestion(request.getParameter(question));
        }

        //setting attributes for the user-time-trial.
        userTimeTrial.setTimeTrial(timeTrial);
        userTimeTrial.setUser(user);

        //adding the user-time-trial to the list userTrials for time-trial.
        timeTrial.getUsersTrials().add(userTimeTrial);

        //adding the user-time-trial to the list trials for the user.
        user.getTrials().add(userTimeTrial);
        user.setAttempts(user.getAttempts() - 1);

        //calling the method finalScore to calculate the trial score for the user.
        userTimeTrial.finalScore();
        //setting the score, attempts, and adding the trial to the list trials for the user.
        user.setTrialScore(userTimeTrial.getScore());


        //Adding the score attribute to the model to be used to display the score
        model.addAttribute("score", userTimeTrial.getScore());


        userTrialRepository.save(userTimeTrial);
        userRepo.save(user);
        timeTrialRepository.save(timeTrial);
        return "redirect:/trials";

    }

    @RequestMapping("/trials/{id}/Feedback")
    public String previousAttempts(Model model, @PathVariable long id) {
        User user = userAuth();
        UserTimeTrial userTimeTrial = userTrialRepository.findById(id).get();
        //Adding the time-trial to be reviewed to the model.
        model.addAttribute("userTrial", userTimeTrial);
        //Adding the questions of the trial to the model
        model.addAttribute("questions", userTimeTrial.getTimeTrial().getQuestionList());
        //Adding the user to the model.
        model.addAttribute("user", user);
        //Adding the userId to the model to apply changes to the header
        model.addAttribute("userId", user.getId());
        //Adding the review attribute to the model,
        // with its value set to true to make the trialQuiz form read only.
        model.addAttribute("review", true);
        return "trialQuiz";
    }


    private User userAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Get the username
        // Retrieve the user by username
        return userRepo.findByUsername(username).orElse(null);
    }
}
