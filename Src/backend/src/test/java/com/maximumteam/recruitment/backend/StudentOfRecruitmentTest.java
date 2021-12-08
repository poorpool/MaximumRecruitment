package com.maximumteam.recruitment.backend;

import com.maximumteam.recruitment.backend.service.StudentOfRecruitmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StudentOfRecruitmentTest {
    @Autowired
    private StudentOfRecruitmentService studentOfRecruitmentService;

    @Test
    void getStudentOfRecruitmentsByUserIdTest() {
        System.out.println(studentOfRecruitmentService.getStudentOfRecruitmentsByUserId("619cda3c7ef0ab3fa22c4236"));
    }
}
