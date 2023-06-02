package com.program.service;

import com.program.exception.DepartmentException;
import com.program.exception.TeacherRateException;
import com.program.model.Department;
import com.program.model.teacher.TeacherRate;
import java.util.List;

public interface RateService {

    List<TeacherRate> findAllRate() throws TeacherRateException;

    TeacherRate getRateById(Integer Id) throws TeacherRateException;
}
