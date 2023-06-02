package com.program.service;

import com.program.exception.ReportException;

import java.io.IOException;

public interface ReportService {

    byte[] generatePdf() throws ReportException, IOException;
}
