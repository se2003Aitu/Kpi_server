package com.program.model.teacher;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class TeacherEventId implements Serializable {
    private Long teacherId;
    private Integer eventId;

    public TeacherEventId() {
        super();
    }

    public TeacherEventId(Long teacherId, Integer eventId) {
        this.teacherId = teacherId;
        this.eventId = eventId;
    }
}
