package com.program.exception;

import java.io.IOException;

public class SubmissionException extends Exception{
    public SubmissionException(String s, IOException ex) {
        super();
    }

    public SubmissionException(String message) {
        super(message);
    }
}
