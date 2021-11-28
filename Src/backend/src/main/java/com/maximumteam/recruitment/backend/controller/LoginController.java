package com.maximumteam.recruitment.backend.controller;

import com.maximumteam.recruitment.backend.entity.Account;
import com.maximumteam.recruitment.backend.entity.ReturnMessage;
import com.maximumteam.recruitment.backend.service.AccountService;
import com.maximumteam.recruitment.backend.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class LoginController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public ReturnMessage login(@RequestBody Account give) {
        String email = give.getEmail();
        String password = give.getPassword();
        Account account = accountService.findAccountByEmail(email);
        if (account == null || !Objects.equals(password, account.getPassword())) {
            return ReturnMessage.fail(400).setMessage("用户名或密码错误");
        }
        String token = JWTUtil.sign(email, password);
        account.setPassword(null);
        return ReturnMessage.success().setParam("token", token).setParam("user", account);
    }
}
