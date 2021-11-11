package com.maximumteam.recruitment.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recruitment implements Serializable {
    @Id
    private String id;

    private String name;

    private Date startTime;

    private Date endTime;

    private List<String> steps;
}
