package com.example.ibm_project_code.repositories;

import com.example.ibm_project_code.database.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Integer> {
    public Course findById(long id);
    public List<Course> findTop3ByOrderByCourseUsersDesc();
    @Query("SELECT DISTINCT c.category FROM Course c")
    List<String> findDistinctCategories();
}
