package com.maximumteam.recruitment.backend;

import com.maximumteam.recruitment.backend.dao.AccountRepository;
import com.maximumteam.recruitment.backend.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DaoTests {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void insertAccountTest() {
        Account account = new Account(null, "admin@maximum.com", "admin", "dfdfsdd", 6);
        accountRepository.save(account);
    }

    @Test
    void findAccountByEmailTest() {
        System.out.println(accountRepository.findAccountByEmail("admin@maximum.com"));
        System.out.println(accountRepository.findAccountByEmail("d"));
    }
}
