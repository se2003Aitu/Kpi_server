package com.program.controller;

import com.program.exception.SubmissionException;
import com.program.exception.UserException;
import com.program.helper.jwt.JwtUtils;
import com.program.helper.util.DateUtils;
import com.program.model.User;
import com.program.model.submission.Submission;
import com.program.service.JwtService;
import com.program.service.SubmissionService;
import com.program.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;



@RestController
public class SubmissionController {

    @Autowired
    public SubmissionService submissionService;

    @Autowired
    public JwtUtils jwtUtils;

    @Autowired
    public UserService userService;

    @Autowired
    public JwtService jwtService;

    @PostMapping("/event/{eventId}/uploads")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity uploadMultipleFiles(HttpServletRequest request, @PathVariable Integer eventId, @RequestParam("files") MultipartFile[] files) {
        try {
            String token = jwtService.extractBearerToken(request);
            String email = jwtUtils.getEmailFromJwtToken(token);
            User user = userService.isUserEmailPresent(email);
            if (user != null) {
                Long userId = user.getUserId();
                Date modifyDate = DateUtils.getCurrentDate();
                for (MultipartFile file : files) {
                    submissionService.saveSubmit(userId, eventId, file,modifyDate);
                }
            }
            return new ResponseEntity<>("The file has been successfully uploaded!", HttpStatus.OK);
        } catch (SubmissionException | UserException ex) {
            String errorMessage = "Error setting: " + ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request,@PathVariable String fileId) throws Exception {

        Submission submission = submissionService.getSubmission(fileId);
        return  ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(submission.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + submission.getFileName()
                                + "\"")
                .body(new ByteArrayResource(submission.getData()));
    }

    @GetMapping("teacher/{teacherId}/event/{eventId}/submissions")
    public ResponseEntity getFile(@PathVariable("teacherId") Long teacherId, @PathVariable("eventId") Integer eventId){
        try {
            List<Submission> submissions = submissionService.getSubmissions(teacherId, eventId);
            return new ResponseEntity<>(submissions,HttpStatus.OK);
        }catch (SubmissionException ex){
            String errorMessage = "Error setting: " + ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/submission/delete/{Id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Object> deleteCategoryById(HttpServletRequest request,@PathVariable ("Id") String id) {
        try {
            String token = jwtService.extractBearerToken(request);
            String email = jwtUtils.getEmailFromJwtToken(token);
            User user = userService.isUserEmailPresent(email);
            if (user != null) {
                Long userId = user.getUserId();
                Date modifyDate = DateUtils.getCurrentDate();
            submissionService.deleteSubmission(userId,id,modifyDate);
            }
            return new ResponseEntity<>("The file with this id deleted", HttpStatus.OK);
        }catch (SubmissionException | UserException ex) {
            String errorMessage = "Error setting: " + ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
