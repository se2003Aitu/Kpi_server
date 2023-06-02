package com.program.model.submission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.program.model.Event;
import com.program.model.teacher.Teacher;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class TeacherSubmission {

    @EmbeddedId
    @JsonIgnore
    private TeacherSubmissionId id;

    @MapsId("teacherId")
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @JsonIgnore
    private Teacher teacher;

    @MapsId("eventId")
    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private Event event;

    @MapsId("submissionId")
    @ManyToOne
    @JoinColumn(name = "submission_id")
    @JsonIgnore
    private Submission submission;

    public TeacherSubmission(TeacherSubmissionId id, Teacher teacher, Event event, Submission submission) {
        this.id = id;
        this.teacher = teacher;
        this.event = event;
        this.submission = submission;
    }

    @JsonProperty("event_id")
    public Integer getEventId(){
        return event.getEventId();
    }

    @JsonProperty("submission_id")
    public String getSubmissionId(){
        return  submission.getSubmissionId();
    }

    @JsonProperty("file_name")
    public String getFileName(){
        return  submission.getFileName();
    }

    @JsonProperty("file_type")
    public String getFileType(){
        return  submission.getFileType();
    }

    @JsonProperty("size")
    public long getFileSize(){
        return  submission.getSize();
    }



}
