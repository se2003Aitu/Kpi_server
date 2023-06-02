package com.program.controller.teacher;

import com.program.exception.TeacherException;
import com.program.model.teacher.Teacher;
import com.program.model.teacher.TeacherEvent;
import com.program.service.TeacherService;
import com.program.service.UserService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
public class TeacherController {

    @Autowired
    public TeacherService teacherService;


    @GetMapping("/teachers")
    public ResponseEntity<List<Teacher>> getAllTeachers() throws TeacherException {
        List<Teacher> teachers = teacherService.findAllTeacher();
        return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);
    }

    @GetMapping("/teacher/getById/{Id}")
    public ResponseEntity getTeacherById(@PathVariable("Id") Long id ){
        try {
            Teacher teacher = teacherService.getTeacherById(id);
            return new ResponseEntity<Teacher>(teacher,HttpStatus.OK);
        }catch (TeacherException ex){
            String errorMessage = "Error: " + ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
