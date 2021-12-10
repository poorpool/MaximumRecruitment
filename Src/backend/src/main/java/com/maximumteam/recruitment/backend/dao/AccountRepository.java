package com.maximumteam.recruitment.backend.dao;

import com.maximumteam.recruitment.backend.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AccountRepository extends PagingAndSortingRepository<Account,Integer> {
    Account findAccountByEmail(String email);

    void deleteByIdIn(List<String> ids);

    List<Account> findAll();
}