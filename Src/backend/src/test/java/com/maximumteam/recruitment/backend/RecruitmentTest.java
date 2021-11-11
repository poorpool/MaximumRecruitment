package com.maximumteam.recruitment.backend;

import com.maximumteam.recruitment.backend.entity.Recruitment;
import com.maximumteam.recruitment.backend.service.RecruitmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class RecruitmentTest {
    @Autowired
    private RecruitmentService recruitmentService;

    @Test
    void saveTest() {
        List<String> steps = new ArrayList<>();
        steps.add("笔试");
        steps.add("面试");
        steps.add("通过");
        Recruitment recruitment = new Recruitment(null, "2021秋季招新", new Date(), new Date(), steps);
        recruitmentService.save(recruitment);
    }

    @Test
    void selectTest() {
        System.out.println(recruitmentService.findAll());
    }

    @Test
    void updateTest() {
        Recruitment recruitment = new Recruitment();
        recruitment.setId("618d08a191284b6b3f2fc3ac");
        recruitment.setEndTime(new Date());
        System.out.println(recruitmentService.update(recruitment));
    }

    @Test
    void deleteTest() {
        Recruitment recruitment = new Recruitment();
        recruitment.setId("618d08a191284b6b3f2fc3ac");
        recruitmentService.delete(recruitment);
    }
}
