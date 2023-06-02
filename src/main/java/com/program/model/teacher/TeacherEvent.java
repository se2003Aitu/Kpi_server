package com.program.model.teacher;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.program.model.Event;
import com.program.model.approve.Approve;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;


@Entity
public class TeacherEvent {

    @EmbeddedId
    private TeacherEventId id;

    @ManyToOne
    @MapsId("teacherId")
    @JoinColumn(name = "teacher_id")
    @JsonIgnore
    private Teacher teacher;

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private Event event;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "approveId", columnDefinition = "integer default 1")
    @JsonIgnore
    private Approve approve;

    private boolean submissionStatus;
    private String eventRate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    private String comment;

    public TeacherEvent() {
        super();
    }

    public TeacherEvent(TeacherEventId id, Teacher teacher, Event event,Approve approve) {
        this.id = id;
        this.teacher = teacher;
        this.event = event;
        this.approve = approve;
    }

    @JsonIgnore
    public Long getTeacherId(){
        return teacher.getTeacherId();
    }

    @JsonProperty("teacher_name")
    public String getTeacherName(){
        return teacher.getUserName();
    }

    @JsonProperty("department_name")
    public String getTeacherDepartment(){
        return teacher.getDepartmentName();
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @JsonProperty("event_id")
    public Integer getEventId(){
        return event.getEventId();
    }

    @JsonProperty("event_name")
    public String getEventName() {
        return event.getEventName();
    }

    @JsonProperty("event_percentage")
    public Integer getEventPercentage(){
        return event.getEventPercentage();
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @JsonProperty("event_rate")
    public String getEventRate() {
        return eventRate;
    }

    public void setEventRate(String eventRate) {
        this.eventRate = eventRate;
    }

    @JsonProperty("approve_name")
    public String getApproveName() {
        return approve.getApproveName();
    }

    @JsonIgnore
    public boolean isAccept() {
        String approveName = "Accepted";
        return Objects.equals(approve.getApproveName(), approveName);
    }

    @JsonIgnore
    public boolean isReject() {
        String approveName = "Rejected";
        return Objects.equals(approve.getApproveName(), approveName);
    }

    @JsonIgnore
    public boolean isNone() {
        String approveName = "none";
        return Objects.equals(approve.getApproveName(), approveName);
    }

    public Approve getApprove() {
        return approve;
    }

    public void setApprove(Approve approve) {
        this.approve = approve;
    }

    @JsonProperty("submission_status")
    public boolean isSubmissionStatus() {
        return submissionStatus;
    }

    @JsonProperty("modify_date")
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm")
    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @JsonIgnore
    public String getEventRates(){
        return event.getEventRates();
    }

    public void setSubmissionStatus(boolean submissionStatus) {
        this.submissionStatus = submissionStatus;
    }

}
