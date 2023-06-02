package com.program.service;

import com.program.exception.DepartmentException;
import com.program.model.Department;

import java.util.List;

public interface DepartmentService {

    List<Department> findAllDepartments() throws DepartmentException;

    Department getDepartmentById(Integer id) throws DepartmentException;

}
