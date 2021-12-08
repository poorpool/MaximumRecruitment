package com.maximumteam.recruitment.backend.service;

import com.maximumteam.recruitment.backend.dao.RecruitmentRepository;
import com.maximumteam.recruitment.backend.entity.Account;
import com.maximumteam.recruitment.backend.entity.Recruitment;
import com.maximumteam.recruitment.backend.util.Utils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    public Recruitment findById(String id) {
        return recruitmentRepository.findById(id);
    }

    public Page<Recruitment> getRecruitmentsByPage(int page, int cnt) {
        if (page < 1) {
            page = 1;
        }
        Page<Recruitment> pages = recruitmentRepository.findAll(PageRequest.of(page - 1, cnt));
        return pages;
    }

    public List<Recruitment> getAllAvailableRecruitments() {
        Date date = new Date();
        Query query = Query.query(Criteria.where("startTime").lte(date)
                .and("endTime").gt(date));
        return mongoTemplate.find(query, Recruitment.class);
    }
}
