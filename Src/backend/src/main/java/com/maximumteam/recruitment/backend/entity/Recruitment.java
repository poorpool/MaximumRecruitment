package com.maximumteam.recruitment.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotBlank(message = "招新名称不能为空")
    private String name;

    @Indexed
    @NotNull(message = "日期不能为空")
    private Date startTime;

    @Indexed
    @NotNull(message = "日期不能为空")
    private Date endTime;

    @NotNull(message = "步骤不能为空")
    private List<String> steps;
}
