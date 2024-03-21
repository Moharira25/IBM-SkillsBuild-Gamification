package com.example.ibm_project_code.repositories;


import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.database.UserCourse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserCourseRepository extends CrudRepository<UserCourse, Long> {
    List<UserCourse> findByUser(User user);


}
