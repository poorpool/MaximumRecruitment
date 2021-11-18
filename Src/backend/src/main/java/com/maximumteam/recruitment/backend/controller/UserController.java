package com.maximumteam.recruitment.backend.controller;

import com.maximumteam.recruitment.backend.entity.Account;
import com.maximumteam.recruitment.backend.entity.ReturnMessage;
import com.maximumteam.recruitment.backend.service.AccountService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public ReturnMessage register(@RequestBody @Validated Account account) {
        account.setPermission(1);
        boolean ret = accountService.save(account);
        if (!ret) {
            return ReturnMessage.fail(400).setMessage("邮箱已被使用");
        } else {
            return ReturnMessage.success();
        }
    }

    @RequestMapping("/test")
    @RequiresRoles(value = {"admin"}, logical = Logical.OR)
    public ReturnMessage boynextdoor() {
        return ReturnMessage.success();
    }

}
