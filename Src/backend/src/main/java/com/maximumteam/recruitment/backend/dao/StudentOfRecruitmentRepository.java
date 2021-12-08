package com.maximumteam.recruitment.backend.dao;

import com.maximumteam.recruitment.backend.entity.StudentOfRecruitment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentOfRecruitmentRepository extends MongoRepository<StudentOfRecruitment, Integer> {
    StudentOfRecruitment findStudentOfRecruitmentsByRecruitmentIdAndUserId(String recruitmentId,
                                                                           String userId);
    List<StudentOfRecruitment> findStudentOfRecruitmentsByUserId(String userId);
}
