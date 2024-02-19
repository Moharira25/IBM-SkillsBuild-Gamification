package com.example.ibm_project_code.controllers;


import com.example.ibm_project_code.database.Course;
import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.database.UserCourse;
import com.example.ibm_project_code.repositories.CourseRepository;
import com.example.ibm_project_code.repositories.UserCourseRepository;
import com.example.ibm_project_code.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;


@Controller
public class CourseController {
    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    UserCourseRepository userCourseRepository;


    @GetMapping("/dashboard")
    public String list(Model model) {
        User user = userAuth();
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("user", user);

        return "dashboard";
    }

    @RequestMapping("/start/{courseId}")
    public String startLearning(@PathVariable int courseId) {
        User user = userAuth();

        Course course = courseRepo.findById(courseId);

        //Creating a course enrollment for the started course and recording the start date.
        UserCourse userCourse = new UserCourse();
        userCourse.setCourse(course);
        //Formatting the date and setting the start date for the user-course.
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedStartDate = simpleFormat.format(Timestamp.from(Instant.now()));
        userCourse.setStartDate(formattedStartDate);
        userCourse.setUser(user);

        //saving the userCourse to the database.
        userCourseRepository.save(userCourse);

        user.getCourses().add(userCourse);
        course.getEnrollments().add(userCourse);

        userRepo.save(user);
        courseRepo.save(course);

        return "redirect:" + course.getLink();
    }

    @RequestMapping("/finish/{courseId}")
    public String finish(@PathVariable int courseId) {
        User user = userAuth();
        Course course = courseRepo.findById(courseId);

        //getting the formatted date
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedEndDate = simpleFormat.format(Timestamp.from(Instant.now()));
        //Recording the completion time for the course.
        user.getUserCourse(course).setEndDate(formattedEndDate);

        //Setting the done attribute to true in the UserCourse class
        user.getUserCourse(course).setDone(true);
        int newPoints = user.getOverallPoints() + course.getPoints();
        user.setOverallPoints(newPoints);

        userRepo.save(user);
        courseRepo.save(course);

        return "redirect:/dashboard";
    }


    //A controller for when a user resume a course.
    @RequestMapping("/resume/{courseId}")
    public String resume(@PathVariable int courseId) {
        Course course = courseRepo.findById(courseId);
        return "redirect:" + course.getLink();
    }


    private User userAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Get the username
        // Retrieve the user by username
        return userRepo.findByUsername(username).orElse(null);
    }
}
