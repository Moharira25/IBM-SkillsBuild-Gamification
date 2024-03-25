package com.example.ibm_project_code.repositories;

import com.example.ibm_project_code.database.TimeTrial;
import org.springframework.data.repository.CrudRepository;

public interface TimeTrialRepository extends CrudRepository<TimeTrial, Long> {
    TimeTrial findByEndedFalse();
}
