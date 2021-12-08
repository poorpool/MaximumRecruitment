package com.maximumteam.recruitment.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentOfRecruitment implements Serializable {

    @Id
    private String id;

    @Indexed
    @NotBlank(message = "招新键不能为空")
    private String recruitmentId;

    @Indexed
    @NotBlank(message = "用户键不能为空")
    private String userId;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotBlank(message = "学院不能为空")
    private String department;

    @NotBlank(message = "专业班级不能为空")
    private String majorAndClass;

    @NotBlank(message = "性别不能为空")
    private String sex;

    @NotBlank(message = "年级不能为空")
    private String grade;

    private String recommender;

    private String introduction;

    private int nowStep;

    private int isEnd;
}
