package com.maximumteam.recruitment.backend;

import com.maximumteam.recruitment.backend.entity.Account;
import com.maximumteam.recruitment.backend.service.AccountService;
import com.maximumteam.recruitment.backend.util.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class AccountTest {
    @Autowired
    private AccountService accountService;

    @Test
    void insertAccountTest() {
        Account account = new Account(null, "admin@maximum.com", "admin", "123456", 6);
        account.setPassword(Utils.SHA256(account.getEmail() + account.getPassword()));
        accountService.save(account);
    }

    @Test
    void batchInsertAccountTest() {
        for (int i = 0; i < 10; i++) {
            String name = "user" + i;
            Account account = new Account(null, name + "@maximum.com", name, "123456", 1);
            account.setPassword(Utils.SHA256(account.getEmail() + account.getPassword()));
            accountService.save(account);
        }
        for (int i = 0; i < 10; i++) {
            String name = "manager" + i;
            Account account = new Account(null, name + "@maximum.com", name, "123456", 2);
            account.setPassword(Utils.SHA256(account.getEmail() + account.getPassword()));
            accountService.save(account);
        }
    }

    @Test
    void findAccountByEmailTest() {
        System.out.println(accountService.findAccountByEmail("admin@maximum.com"));
        System.out.println(accountService.findAccountByEmail(""));
        System.out.println(accountService.findAccountByEmail(null));
    }

    @Test
    void updateAccountTest() {
        Account account = new Account("618c7a2965a40c2cc3e84d18", "fakeEmail", null, "yes", 6);
        System.out.println(accountService.update(account));

        Account account1 = new Account("", "fakeEmail", null, "door", 6);
        System.out.println(accountService.update(account1));
    }

    @Test
    void deleteAccountTest() {
        Account account = new Account();
        account.setId("618c7a2965a40c2cc3e84d18");
        accountService.delete(account);
    }

    @Test
    void batchDeleteAccountTest() {
        List<String> list = new ArrayList<>();
        list.add("618c8071cfec681b3eea8160");
        list.add("618c8071cfec681b3eea815f");
        accountService.batchDelete(list);
    }

    @Test
    void batchModifyAccountPermissionTest() {
        List<String> list = new ArrayList<>();
        list.add("618c8071cfec681b3eea8164");
        list.add("618c8071cfec681b3eea8165");
        list.add("fakeId");
        System.out.println(accountService.batchModifyPermission(list, 1));
    }
}
