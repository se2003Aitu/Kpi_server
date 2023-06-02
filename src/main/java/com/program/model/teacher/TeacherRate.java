package com.program.model.teacher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.program.model.Status;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TeacherRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer teacherRateId;

    private String teacherRateName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacherRate")
    private List<Teacher> teachers =new ArrayList<>();

    @JsonIgnore
    public Integer getTeacherRateId() {
        return teacherRateId;
    }

    public void setTeacherRateId(Integer teacherRateId) {
        this.teacherRateId = teacherRateId;
    }

    @JsonProperty("teacher_rate")
    public String getTeacherRateName() {
        return teacherRateName;
    }

    public void setTeacherRateName(String teacherRateName) {
        this.teacherRateName = teacherRateName;
    }
}
