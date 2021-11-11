package com.maximumteam.recruitment.backend.dao;

import com.maximumteam.recruitment.backend.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account,Integer> {
    public Account findAccountByEmail(String email);
}