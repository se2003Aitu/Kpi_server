package com.program.service.serviceImpl;

import com.program.exception.DepartmentException;
import com.program.exception.TeacherRateException;
import com.program.model.Department;
import com.program.model.teacher.TeacherRate;
import com.program.repository.TeacherRateRepository;
import com.program.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RateServiceImpl implements RateService {

    @Autowired
    private TeacherRateRepository teacherRateRepository;

    @Override
    public List<TeacherRate> findAllRate() throws TeacherRateException {
        return teacherRateRepository.findAll();
    }

    @Override
    public TeacherRate getRateById(Integer id) throws TeacherRateException {
        Optional<TeacherRate> opt= teacherRateRepository.findById(id);
        if(opt.isPresent()) {
            return opt.get();
        }
        else {
            throw new TeacherRateException("Rate does not exist with Id: " + id);
        }
    }
}
