package com.program.controller;

import com.program.exception.TeacherException;
import com.program.exception.UserException;
import com.program.helper.jwt.JwtUtils;
import com.program.model.teacher.Teacher;
import com.program.model.User;
import com.program.service.JwtService;
import com.program.service.TeacherService;
import com.program.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ProfileController {

    @Autowired
    public UserService userService;

    @Autowired
    public TeacherService teacherService;

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    public JwtUtils jwtUtils;

    @Autowired
    public JwtService jwtService;


    @GetMapping("/profile")
    @PreAuthorize("hasRole('TEACHER')")
    public HttpEntity<? extends Object> getUser(HttpServletRequest request) throws TeacherException, UserException {
        String token = jwtService.extractBearerToken(request);
        String email = jwtUtils.getEmailFromJwtToken(token);
        User user = userService.isUserEmailPresent(email);
        if (user != null) {
                Long id = user.getUserId();
                Teacher teacher = teacherService.getTeacherByUserId(id);
                return new ResponseEntity<Teacher>(teacher, HttpStatus.OK);
            }

        return new ResponseEntity<>("Bad credentials", HttpStatus.UNAUTHORIZED);

    }

}
