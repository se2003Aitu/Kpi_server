package com.program.controller;

import com.program.exception.DepartmentException;
import com.program.model.Department;
import com.program.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DepartmentController {

    @Autowired
    public DepartmentService departmentService;

    @GetMapping("/departments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OBSERVER')")
    public ResponseEntity<List<Department>> getAllDepartments() throws DepartmentException {
        List<Department> departments = departmentService.findAllDepartments();
        return new ResponseEntity<List<Department>>(departments, HttpStatus.OK);
    }

    @GetMapping("/department/getById/{Id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OBSERVER')")
    public ResponseEntity getDepartmentById(@PathVariable("Id") Integer id ){
        try {
            Department department = departmentService.getDepartmentById(id);
            return new ResponseEntity<Department>(department,HttpStatus.OK);
        }catch (DepartmentException ex){
            String errorMessage = "Error: " + ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
