package com.example.ibm_project_code.repositories;

import com.example.ibm_project_code.database.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Integer> {
    public Course findById(int id);
}
