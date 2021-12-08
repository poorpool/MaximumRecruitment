package com.maximumteam.recruitment.backend.controller;

import com.maximumteam.recruitment.backend.entity.Account;
import com.maximumteam.recruitment.backend.entity.Recruitment;
import com.maximumteam.recruitment.backend.entity.ReturnMessage;
import com.maximumteam.recruitment.backend.service.RecruitmentService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recruitment")
public class RecruitmentController {
    @Autowired
    private RecruitmentService recruitmentService;

    @PostMapping("/upsert")
    @RequiresRoles(value = {"admin"}, logical = Logical.OR)
    public ReturnMessage upsert(@RequestBody @Validated Recruitment recruitment) {
        String id = recruitment.getId();
        System.out.println(recruitment);
        boolean result = true;
        if (id == null || id.equals("")) {
            recruitmentService.save(recruitment);
        } else {
            result = recruitmentService.update(recruitment);
        }
        if (result) {
            return ReturnMessage.success();
        } else {
            return ReturnMessage.fail(400).setMessage("更新失败");
        }
    }

    @RequestMapping("/get")
    @RequiresRoles(value = {"admin", "manager", "user"}, logical = Logical.OR)
    public ReturnMessage getRecruitmentsByPage(@RequestParam int page, @RequestParam int itemsPerPage) {
        Page<Recruitment> ret = recruitmentService.getRecruitmentsByPage(page, itemsPerPage);
        return ReturnMessage.success().setParam("total", ret.getTotalElements())
                .setParam("recruitments", ret.toList());
    }

    @DeleteMapping("/{id}")
    @RequiresRoles(value = {"admin"}, logical = Logical.OR)
    public ReturnMessage deleteRecruitment(@PathVariable String id) {
        Recruitment recruitment = new Recruitment();
        recruitment.setId(id);
        recruitmentService.delete(recruitment);
        return ReturnMessage.success();
    }
}
