package com.program.service;

import com.program.exception.TeacherEventException;
import com.program.exception.TeacherException;
import com.program.model.approve.Approve;
import com.program.model.teacher.TeacherEvent;

import java.util.List;

public interface TeacherEventService {

    List<TeacherEvent> getAllTeacherEvent() throws TeacherEventException;

    List<TeacherEvent> getTeachersByEvent(Integer eventId) throws TeacherEventException;

    void setEventApprove(Long teacherId, Integer eventId, Approve approve) throws TeacherEventException;

    void addComment(Long userId, Integer eventId, String comment) throws TeacherEventException;

    void deleteComment(Long userId, Integer eventId) throws TeacherEventException;


}
