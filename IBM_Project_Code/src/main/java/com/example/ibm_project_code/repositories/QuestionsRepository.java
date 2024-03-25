package com.example.ibm_project_code.repositories;

import com.example.ibm_project_code.database.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionsRepository extends CrudRepository<Question, Long> {
}
