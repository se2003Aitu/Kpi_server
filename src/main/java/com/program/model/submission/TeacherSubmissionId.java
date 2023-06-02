package com.program.model.submission;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class TeacherSubmissionId implements Serializable {
    private Long teacherId;
    private Integer eventId;
    private String submissionId;


    public TeacherSubmissionId() {
    }

    public TeacherSubmissionId(Long teacherId, Integer eventId, String submissionId) {
        this.teacherId = teacherId;
        this.eventId = eventId;
        this.submissionId = submissionId;
    }
}