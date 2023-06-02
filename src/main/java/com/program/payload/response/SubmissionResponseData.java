package com.program.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionResponseData {
    private String fileName;
//    private String downloadURL;
    private String fileType;
    private long fileSize;
}
