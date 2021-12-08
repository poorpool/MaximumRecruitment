package com.maximumteam.recruitment.backend.dao;

import com.maximumteam.recruitment.backend.entity.Recruitment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecruitmentRepository extends PagingAndSortingRepository<Recruitment,Integer> {
}
