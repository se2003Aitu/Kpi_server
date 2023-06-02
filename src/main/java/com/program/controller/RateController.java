package com.program.controller;

import com.program.exception.DepartmentException;
import com.program.exception.TeacherRateException;
import com.program.model.Department;
import com.program.model.teacher.TeacherRate;
import com.program.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RateController {

    @Autowired
    public RateService rateService;

    @GetMapping("/rates")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OBSERVER')")
    public ResponseEntity<List<TeacherRate>> getAllRates() throws TeacherRateException {
        List<TeacherRate> teacherRates = rateService.findAllRate();
        return new ResponseEntity<List<TeacherRate>>(teacherRates, HttpStatus.OK);
    }

    @GetMapping("/rate/getById/{Id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OBSERVER')")
    public ResponseEntity getRateById(@PathVariable("Id") Integer id ){
        try {
            TeacherRate teacherRate = rateService.getRateById(id);
            return new ResponseEntity<TeacherRate>(teacherRate,HttpStatus.OK);
        }catch (TeacherRateException ex){
            String errorMessage = "Error: " + ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
