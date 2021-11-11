package com.maximumteam.recruitment.backend.service;

import com.maximumteam.recruitment.backend.dao.RecruitmentRepository;
import com.maximumteam.recruitment.backend.entity.Recruitment;
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
public class RecruitmentService {
    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(Recruitment recruitment) {
        recruitmentRepository.save(recruitment);
    }

    /**
     * 更新招新，但是不更新步骤
     * @param recruitment
     * @return
     */
    public boolean update(Recruitment recruitment) {
        recruitment.setSteps(null);
        Query query = Query.query(Criteria.where("_id").is(recruitment.getId()));
        DBObject dbObject = new BasicDBObject();
        mongoTemplate.getConverter().write(recruitment, (Bson) dbObject);
        Update update = Utils.fromDBObjectExcludeNullFields(dbObject);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Recruitment.class);
        return updateResult.getMatchedCount() > 0;
    }

    public void delete(Recruitment recruitment) {
        recruitmentRepository.delete(recruitment);
    }

    public List<Recruitment> findAll() {
        return recruitmentRepository.findAll();
    }


}