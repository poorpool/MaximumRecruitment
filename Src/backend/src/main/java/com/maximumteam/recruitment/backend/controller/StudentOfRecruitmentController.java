package com.maximumteam.recruitment.backend.controller;

import com.maximumteam.recruitment.backend.entity.Recruitment;
import com.maximumteam.recruitment.backend.entity.ReturnMessage;
import com.maximumteam.recruitment.backend.entity.StudentOfRecruitment;
import com.maximumteam.recruitment.backend.service.RecruitmentService;
import com.maximumteam.recruitment.backend.service.StudentOfRecruitmentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
class BatchUpdateparams {
    private List<String> ids;
    private int nowStep;
    private int isEnd;
}

@RestController
@RequestMapping("/studentOfRecruitment")
public class StudentOfRecruitmentController {
    @Autowired
    private StudentOfRecruitmentService studentOfRecruitmentService;

    @Autowired
    private RecruitmentService recruitmentService;

    @GetMapping("/getByRecruitmentAndUser")
    @RequiresRoles(value = {"admin", "manager", "user"}, logical = Logical.OR)
    public ReturnMessage getStudentOfRecruitmentByRecAndStu(@RequestParam String recruitmentId,
                                                            @RequestParam String userId) {
        return ReturnMessage.success().setParam("sor",
                studentOfRecruitmentService.getStudentOfRecruitmentByRecAndStu(recruitmentId, userId));
    }

    @PostMapping("/upsert")
    @RequiresRoles(value = {"admin", "manager", "user"}, logical = Logical.OR)
    public ReturnMessage upsert(@RequestBody @Validated StudentOfRecruitment studentOfRecruitment) {
        String id = studentOfRecruitment.getId();
        boolean result = true;
        if (id == null || id.equals("")) {
            studentOfRecruitmentService.save(studentOfRecruitment);
        } else {
            result = studentOfRecruitmentService.update(studentOfRecruitment);
        }
        if (result) {
            return ReturnMessage.success();
        } else {
            return ReturnMessage.fail(400).setMessage("更新失败");
        }
    }

    @GetMapping("/getsByUserId")
    @RequiresRoles(value = {"admin", "manager", "user"}, logical = Logical.OR)
    public ReturnMessage getStudentOfRecruitmentsByUserId(@RequestParam String userId) {
        List<StudentOfRecruitment> list = studentOfRecruitmentService.getStudentOfRecruitmentsByUserId(userId);
        List<Recruitment> recruitments = new ArrayList<>();
        for (StudentOfRecruitment sor : list) {
            recruitments.add(recruitmentService.findById(sor.getRecruitmentId()));
        }
        return ReturnMessage.success().setParam("sors", list).setParam("recruitments", recruitments);
    }


    @GetMapping("/getsByRecruitmentId")
    @RequiresRoles(value = {"admin", "manager", "user"}, logical = Logical.OR)
    public ReturnMessage getStudentOfRecruitmentsByRecruitmentId(@RequestParam String recruitmentId) {
        List<StudentOfRecruitment> list = studentOfRecruitmentService.getStudentOfRecruitmentsByRecruitmentId(recruitmentId);
        return ReturnMessage.success().setParam("sors", list);
    }

    
    @PostMapping("/batchUpdate")
    @RequiresRoles(value = {"manager"}, logical = Logical.OR)
    public ReturnMessage batchUpdateByIds(@RequestBody BatchUpdateparams batchUpdateparams) {
        studentOfRecruitmentService.batchUpdateByIds(batchUpdateparams.getIds(), batchUpdateparams.getNowStep(), batchUpdateparams.getIsEnd());
        return ReturnMessage.success();
    }
}
