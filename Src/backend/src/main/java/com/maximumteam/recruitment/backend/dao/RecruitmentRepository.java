package com.maximumteam.recruitment.backend.dao;

import com.maximumteam.recruitment.backend.entity.Recruitment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecruitmentRepository extends MongoRepository<Recruitment,Integer> {
}
