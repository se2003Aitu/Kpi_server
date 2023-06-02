package com.program.service.serviceImpl;

import com.program.exception.DepartmentException;
import com.program.exception.TeacherException;
import com.program.model.Department;
import com.program.model.teacher.Teacher;
import com.program.repository.DepartmentRepository;
import com.program.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public List<Department> findAllDepartments() throws DepartmentException {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Integer id) throws DepartmentException {
        Optional<Department> opt= departmentRepository.findById(id);
        if(opt.isPresent()) {
            return opt.get();
        }
        else {
            throw new DepartmentException("Department does not exist with Id: " + id);
        }
    }
}
