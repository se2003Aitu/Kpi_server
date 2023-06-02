package com.program.controller;

import com.program.exception.ReportException;
import com.program.exception.SubmissionException;
import com.program.exception.UserException;
import com.program.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class ReportController {

    @Autowired
    public ReportService reportService;

    @GetMapping(value = "/report", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity generatePdf() {
        try {
            byte[] pdfBytes = reportService.generatePdf();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "report.pdf");
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        }catch (IOException | ReportException ex) {
            String errorMessage = "Error setting: " + ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
