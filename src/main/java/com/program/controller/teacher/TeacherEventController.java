package com.program.controller.teacher;

import com.program.exception.TeacherEventException;
import com.program.model.approve.Approve;
import com.program.model.teacher.TeacherEvent;
import com.program.service.TeacherEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeacherEventController {

    @Autowired
    public TeacherEventService teacherEventService;


    @GetMapping("/teacher/events")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OBSERVER')")
    public ResponseEntity<List<TeacherEvent>> getAllCategories() throws TeacherEventException {
        List<TeacherEvent> teacherEvents =	teacherEventService.getAllTeacherEvent();
        return new ResponseEntity<List<TeacherEvent>>(teacherEvents,HttpStatus.OK);
    }

    @GetMapping("event/{eventId}/teachers")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OBSERVER')")
    public ResponseEntity getTeachersByEvents( @PathVariable("eventId") Integer eventId ) {
        try {
            List<TeacherEvent> teacherEvents = teacherEventService.getTeachersByEvent(eventId);
            return new ResponseEntity<List<TeacherEvent>>(teacherEvents, HttpStatus.OK);
        }catch (TeacherEventException ex){
            String errorMessage = "Error: " + ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/teacher/{teacherId}/event/{eventId}/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OBSERVER')")
    public ResponseEntity setApproveByEvent(@PathVariable("teacherId") Long teacherId, @PathVariable("eventId") Integer eventId, @RequestBody Approve approve) throws TeacherEventException {
        try {
            teacherEventService.setEventApprove(teacherId, eventId, approve);
            return new ResponseEntity<>("The Approve status has been confirmed for the event with this id " + eventId, HttpStatus.OK);
        } catch (TeacherEventException ex) {
            String errorMessage = "Error setting the event approve status: " + ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
