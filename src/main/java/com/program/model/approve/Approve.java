package com.program.model.approve;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.program.model.teacher.TeacherEvent;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Approve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="approve_id")
    private Integer approveId;

    private String approveName;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "approve")
    private List<TeacherEvent> teacherEvents =new ArrayList<>();

    @JsonIgnore
    public Integer getApproveId() {
        return approveId;
    }

    public void setApproveId(Integer approveId) {
        this.approveId = approveId;
    }

    @JsonProperty("approve_name")
    public String getApproveName() {
        return approveName;
    }

    public void setApproveName(String approveName) {
        this.approveName = approveName;
    }


    @JsonIgnore
    public List<TeacherEvent> getTeacherEvents() {
        return teacherEvents;
    }

    public void setTeacherEvents(List<TeacherEvent> teacherEvents) {
        this.teacherEvents = teacherEvents;
    }
}
