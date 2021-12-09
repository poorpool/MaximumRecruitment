package com.maximumteam.recruitment.backend.controller;

import com.maximumteam.recruitment.backend.entity.Account;
import com.maximumteam.recruitment.backend.entity.ReturnMessage;
import com.maximumteam.recruitment.backend.service.AccountService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AccountService accountService;

    @RequestMapping("/get")
    @RequiresRoles(value = {"admin"}, logical = Logical.OR)
    public ReturnMessage getAccountsByPage(@RequestParam int page, @RequestParam int itemsPerPage) {
        Page<Account> ret = accountService.getAccountsByPage(page, itemsPerPage);
        for (Account account : ret.toList()) {
            account.setPassword("");
        }
        return ReturnMessage.success().setParam("total", ret.getTotalElements())
                .setParam("accounts", ret.toList());
    }


    @DeleteMapping("/{id}")
    @RequiresRoles(value = {"admin"}, logical = Logical.OR)
    public ReturnMessage deleteAccounts(@PathVariable String id) {
        Account account = new Account();
        account.setId(id);
        accountService.delete(account);
        return ReturnMessage.success();
    }

    @PutMapping("/update")
    @RequiresRoles(value = {"admin"}, logical = Logical.OR)
    public ReturnMessage update(@RequestBody Account account) {
        if (account.getPermission() < 0 || account.getPermission() > 3) {
            return ReturnMessage.fail(400).setMessage("权限不合法");
        }
        accountService.update(account);
        return ReturnMessage.success();
    }

}
