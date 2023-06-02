package com.program.controller;

import com.program.exception.TeacherEventException;
import com.program.exception.UserException;
import com.program.helper.jwt.JwtUtils;
import com.program.model.User;
import com.program.payload.request.CommentRequest;
import com.program.service.JwtService;
import com.program.service.TeacherEventService;
import com.program.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CommentController {

    @Autowired
    public TeacherEventService teacherEventService;

    @Autowired
    public UserService userService;

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    public JwtUtils jwtUtils;

    @Autowired
    public JwtService jwtService;

    @PostMapping("teacher/comment/{eventId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addComment(HttpServletRequest request,@RequestBody CommentRequest commentRequest, @PathVariable Integer eventId) throws UserException, TeacherEventException {

        String token = jwtService.extractBearerToken(request);
        String email = jwtUtils.getEmailFromJwtToken(token);
        User user = userService.isUserEmailPresent(email);

        if (user != null) {
            Long userId = user.getUserId();
            teacherEventService.addComment(userId,eventId,commentRequest.getComment());
            return new ResponseEntity<String>("A comment has been added", HttpStatus.OK);
        }else
        {
            return new ResponseEntity<String>("Bad request", HttpStatus.OK);
        }
    }

    @DeleteMapping("teacher/comment/{eventId}/delete")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deleteComment(HttpServletRequest request, @PathVariable Integer eventId) throws UserException, TeacherEventException {

        String token = jwtService.extractBearerToken(request);
        String email = jwtUtils.getEmailFromJwtToken(token);
        User user = userService.isUserEmailPresent(email);

        if (user != null) {
            Long userId = user.getUserId();
            teacherEventService.deleteComment(userId,eventId);
            return new ResponseEntity<String>("A comment has been deleted", HttpStatus.OK);
        }else
        {
            return new ResponseEntity<String>("Bad request", HttpStatus.OK);
        }
    }


}
