package com.maximumteam.recruitment.backend.dao;

import com.maximumteam.recruitment.backend.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccountRepository extends MongoRepository<Account,Integer> {
    Account findAccountByEmail(String email);

    void deleteByIdIn(List<String> ids);
}