package com.example.ibm_project_code.controllers;

import com.example.ibm_project_code.database.Course;
import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.repositories.CourseRepository;
import com.example.ibm_project_code.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class CourseController {
    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private UserRepository userRepo;



    @GetMapping("/dashboard")
    public String list(Model model, @RequestParam Long userId) {
        Map<String, List<Course>> coursesByCategory = new HashMap<>();
        for (Course course : courseRepo.findAll()) {
            String category = course.getCategory();

            //adding a category-courses pair if they are not present in the hashmap
            coursesByCategory.putIfAbsent(category, new ArrayList<>());

            // Add the course to the list from the pair above
            coursesByCategory.get(category).add(course);
        }
        //model.addAttribute("coursesByCategory", coursesByCategory);
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("userId", userId);
        return "dashboard";
    }

    @GetMapping("/myCourses")
    public String myCourses(Model model, @RequestParam Long userId) {
        List<Course> courses = userRepo.findById(userId).get().getCourses();
        if (courses.isEmpty()){
            model.addAttribute("userId", userId);
            return "courseError";
        }
        model.addAttribute("studentCourses", courses);
        model.addAttribute("userId", userId);
        return "myCourses";
    }

    @RequestMapping("/start/{userId}/{courseId}")
    public String startLearning(@PathVariable Long userId, @PathVariable int courseId) {
        Course course = courseRepo.findById(courseId);
        Optional<User> user = userRepo.findById(userId);
        if (user.isPresent()) {
            user.get().getCourses().add(course);
            course.getUsers().add(user.get());
            userRepo.save(user.get());
            courseRepo.save(course);
        }
        return "redirect:"+course.getLink();
    }

    @RequestMapping("/finish/{userId}/{courseId}")
    public String finish(@PathVariable Long userId, @PathVariable int courseId) {
        Course course = courseRepo.findById(courseId);
        Optional<User> user = userRepo.findById(userId);
        if (user.isPresent()) {
            user.get().getCourses().remove(course);
            course.getUsers().remove(user.get());
            int newPoints = user.get().getOverallPoints() + course.getPoints();
            user.get().setOverallPoints(newPoints);
            userRepo.save(user.get());
            courseRepo.save(course);
        }
        return "redirect:/myCourses?userId=" + userId;
    }
}
