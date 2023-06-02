package com.program.service;

import com.program.exception.TeacherException;
import com.program.model.teacher.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherService {

    List<Teacher> findAllTeacher() throws TeacherException;

    Teacher getTeacherById(Long id) throws TeacherException;

    Teacher getTeacherByUserId(Long id) throws TeacherException;


}
