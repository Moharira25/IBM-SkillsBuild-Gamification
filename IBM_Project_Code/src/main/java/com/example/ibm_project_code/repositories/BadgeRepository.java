package com.example.ibm_project_code.repositories;

import com.example.ibm_project_code.database.Badge;
import org.springframework.data.repository.CrudRepository;

public interface BadgeRepository extends CrudRepository<Badge, Integer>{
    public Badge findById(int id);
}
