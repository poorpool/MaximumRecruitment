package com.maximumteam.recruitment.backend.dao;

import com.maximumteam.recruitment.backend.entity.Recruitment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RecruitmentRepository extends PagingAndSortingRepository<Recruitment,Integer> {
    Recruitment findById(String id);

    List<Recruitment> findAll();
}
