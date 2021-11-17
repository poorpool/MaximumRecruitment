package com.maximumteam.recruitment.backend.service;

import com.maximumteam.recruitment.backend.dao.AccountRepository;
import com.maximumteam.recruitment.backend.entity.Account;
import com.maximumteam.recruitment.backend.util.Utils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Account findAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    public boolean save(Account account) {
        if (findAccountByEmail(account.getEmail()) != null) {
            return false;
        }
        accountRepository.save(account);
        return true;
    }

    /**
     * 更新账户。为空的字段不会被更新。email 不会被更新
     * @param account id 不为空的账户
     */
    public boolean update(Account account) {
        account.setEmail(null);
        Query query = Query.query(Criteria.where("_id").is(account.getId()));
        DBObject dbObject = new BasicDBObject();
        mongoTemplate.getConverter().write(account, (Bson) dbObject);
        Update update = Utils.fromDBObjectExcludeNullFields(dbObject);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Account.class);
        return updateResult.getMatchedCount() > 0;
    }

    public void delete(Account account) {
        accountRepository.delete(account);
    }

    public void batchDelete(List<String> ids) {
        accountRepository.deleteByIdIn(ids);
    }

    public boolean batchModifyPermission(List<String> ids, int permission) {
        Query query = Query.query(Criteria.where("_id").in(ids));
        Update update = new Update();
        update.set("permission", permission);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, Account.class);
        return updateResult.getMatchedCount() > 0;
    }
}
