package com.maximumteam.recruitment.backend.service;

import com.maximumteam.recruitment.backend.dao.StudentOfRecruitmentRepository;
import com.maximumteam.recruitment.backend.entity.Recruitment;
import com.maximumteam.recruitment.backend.entity.ReturnMessage;
import com.maximumteam.recruitment.backend.entity.StudentOfRecruitment;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentOfRecruitmentService {
    @Autowired
    private StudentOfRecruitmentRepository studentOfRecruitmentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public StudentOfRecruitment getStudentOfRecruitmentByRecAndStu(String recruitmentId,
                                                                   String userId) {
        return studentOfRecruitmentRepository.findStudentOfRecruitmentsByRecruitmentIdAndUserId(recruitmentId, userId);
    }

    public void save(StudentOfRecruitment studentOfRecruitment) {
        studentOfRecruitmentRepository.save(studentOfRecruitment);
    }

    public boolean update(StudentOfRecruitment studentOfRecruitment) {
        Query query = Query.query(Criteria.where("_id").is(studentOfRecruitment.getId()));
        DBObject dbObject = new BasicDBObject();
        mongoTemplate.getConverter().write(studentOfRecruitment, (Bson) dbObject);
        Update update = Utils.fromDBObjectExcludeNullFields(dbObject);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, StudentOfRecruitment.class);
        return updateResult.getMatchedCount() > 0;
    }

    public List<StudentOfRecruitment> getStudentOfRecruitmentsByUserId(String userId) {
        return studentOfRecruitmentRepository.findStudentOfRecruitmentsByUserId(userId);
    }

    public List<StudentOfRecruitment> getStudentOfRecruitmentsByRecruitmentId(String recruitmentId) {
        return studentOfRecruitmentRepository.findStudentOfRecruitmentsByRecruitmentId(recruitmentId);
    }

    public boolean batchUpdateByIds(@RequestParam List<String> ids, @RequestParam int nowStep, @RequestParam int isEnd) {
        Query query = Query.query(Criteria.where("_id").in(ids));
        StudentOfRecruitment studentOfRecruitment = new StudentOfRecruitment();
        studentOfRecruitment.setNowStep(nowStep);
        studentOfRecruitment.setIsEnd(isEnd);
        DBObject dbObject = new BasicDBObject();
        mongoTemplate.getConverter().write(studentOfRecruitment, (Bson) dbObject);
        Update update = Utils.fromDBObjectExcludeNullFields(dbObject);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, StudentOfRecruitment.class);
        return updateResult.getMatchedCount() > 0;
    }
}
